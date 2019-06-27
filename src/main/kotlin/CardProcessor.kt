import api.Api

class CardProcessor(private val api: Api) {
    fun getCards(owner: String): List<Card> {
        println("Finding cards for $owner")
        val repos = getRepos(owner)
        val repoMap = createRepoMap(repos)
        println("Found ${repos.size} repos")

        val epics = getAllEpics(repos)
        val zenIssueMap = createZenIssueMap(epics)
        println("Found ${epics.size} epics")

        val githubIssues = getAllGithubIssues(owner, repos)
        val githubIssueMap = createGithubIssueMap(githubIssues, zenIssueMap, owner, repoMap)
        println("Found ${githubIssues.size} issues")

        return getAllCards(githubIssues, zenIssueMap, githubIssueMap, owner)
    }

    private fun getRepos(owner: String) : List<GithubRepo> {
        val repos = api.getGithubReposByOrg(owner)
        return if (repos.isNotEmpty()) {
            repos
        } else {
            println("Trying as user instead of organization")
            api.getGithubReposByUser(owner)
        }
    }

    private fun createRepoMap(repos: List<GithubRepo>): Map<Int, GithubRepo> {
        val map = mutableMapOf<Int, GithubRepo>()
        repos.forEach { map[it.id] = it }
        return map
    }


    private fun getAllEpics(repos: List<GithubRepo>): List<Epic> {
        return repos.map { repo ->
            api.getEpicsIds(repo.id, 0).mapNotNull { epicId ->
                val epic = api.getEpic(repo.id, epicId, 0)
                epic?.repo_id = repo.id
                epic?.issue_number = epicId
                epic?.issues?.forEach {
                    it.epic = epic
                }
                epic

            }
        }.flatten()
    }

    private fun createZenIssueMap(epics: List<Epic>): Map<Int, Map<Int, ZenIssue>> {
        val map = mutableMapOf<Int, MutableMap<Int, ZenIssue>>()

        epics.forEach { epic ->
            epic.issues.forEach { issue ->
                map.putIfAbsent(issue.repo_id, mutableMapOf())
                map[issue.repo_id]!![issue.issue_number] = issue
            }
        }

        return map
    }

    private fun getAllGithubIssues(owner: String, repos: List<GithubRepo>): List<GithubIssue> {
        return repos.map { repo ->
            println("Fetching issues for repo ${repo.name}")
            val issues = api.getGithubIssues(owner, repo.name).filter { it.pull_request == null }
            issues.forEach {
                it.repoId = repo.id
                it.repoName = repo.name
            }
            issues
        }.flatten()
    }

    private fun createGithubIssueMap(issues: List<GithubIssue>, zenIssueMap: Map<Int, Map<Int, ZenIssue>>, owner: String, repoMap: Map<Int, GithubRepo>): Map<Int, Map<Int, GithubIssue>> {
        val map = mutableMapOf<Int, MutableMap<Int, GithubIssue>>()
        issues.forEach { issue ->
            map.putIfAbsent(issue.repoId, mutableMapOf())
            map[issue.repoId]!![issue.number] = issue
        }

        issues.forEach { issue ->
            addMissingEpicIssue(zenIssueMap, issue, repoMap, owner, map)
        }

        return map
    }

    /**
     * Github does not seem to be returning ALL issues for a repo. This mess is to manually compensate for epic issues that are missing from the get all issues call.
     */
    private fun addMissingEpicIssue(zenIssueMap: Map<Int, Map<Int, ZenIssue>>, issue: GithubIssue, repoMap: Map<Int, GithubRepo>, owner: String, githubIssues: MutableMap<Int, MutableMap<Int, GithubIssue>>) {
        val epic = zenIssueMap[issue.repoId]?.get(issue.number)?.epic
        if (epic != null && githubIssues[epic.repo_id]?.get(epic.issue_number) == null) {
            val repoName = repoMap[epic.repo_id]?.name
            if (repoName != null) {
                val githubEpic = api.getGithubIssue(owner, repoName, epic.issue_number)
                if (githubEpic != null) {
                    githubEpic.repoId = epic.repo_id
                    githubEpic.repoName = repoName
                    githubIssues.putIfAbsent(githubEpic.repoId, mutableMapOf())
                    githubIssues[githubEpic.repoId]!![githubEpic.number] = githubEpic
                }
            }
        }
    }

    private fun getAllCards(githubIssues: List<GithubIssue>, zenIssueMap: Map<Int, Map<Int, ZenIssue>>, githubIssueMap: Map<Int, Map<Int, GithubIssue>>, owner: String): List<Card> {
        return githubIssues.mapNotNull { githubIssue ->
            getCardInfo(githubIssue, zenIssueMap, githubIssueMap, owner)
        }
    }

    private fun getCardInfo(githubIssue: GithubIssue, zenIssueMap: Map<Int, Map<Int, ZenIssue>>, githubIssueMap: Map<Int, Map<Int, GithubIssue>>, owner: String): Card? {
        val zenIssue = zenIssueMap[githubIssue.repoId]?.get(githubIssue.number)
        val epic = zenIssue?.epic
        val epicTitle = when {
            epic != null -> {
                val githubEpic = githubIssueMap[epic.repo_id]?.get(epic.issue_number)
                if (githubEpic != null) {
                    githubEpic.title
                } else {
                    println("Couldn't find github issue for epic ${epic.repo_id} ${epic.issue_number}")
                    ""
                }
            }
            isEpic(githubIssue) -> githubIssue.title
            else -> ""
        }

        return createCard(githubIssue, zenIssue, githubIssue.repoName, epicTitle, owner)
    }

    private fun isEpic(githubIssue: GithubIssue): Boolean {
        return githubIssue.labels.any { it.name.toLowerCase() == "epic" }
    }

    private fun createCard(githubIssue: GithubIssue, zenIssue: ZenIssue?, repoName: String, epicTitle: String, owner: String): Card? {
        val assignees = githubIssue.assignees.map { it.login }
        val labels = githubIssue.labels.map { it.name }
        val description = cleanNewLines(githubIssue.body)
        val updatedAt = formatDate(githubIssue.updated_at)

        return Card(githubIssue.id, owner, repoName, githubIssue.number, githubIssue.title, description, zenIssue?.estimate?.value?.toString()
                ?: "", epicTitle, githubIssue.milestone?.title ?: "", zenIssue?.pipeline?.name
                ?: "", updatedAt, assignees, labels)
    }




}

