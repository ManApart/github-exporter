import api.Api
import java.io.File

class CardPrinter(private val api: Api) {
    var fileName = "unknown"


    fun getCards(owner: String): List<Card> {
        println("Finding cards for $owner")
        val repos = api.getGithubRepos(owner)
        println("Found ${repos.size} repos")

        val epics = getAllEpics(repos)
        val zenIssueMap = createZenIssueMap(epics)
        println("Found ${epics.size} epics")

        val githubIssues = getAllGithubIssues(owner, repos)
        val githubIssueMap = createGithubIssueMap(githubIssues)
        println("Found ${githubIssues.size} issues")

        return getAllCards(githubIssues, zenIssueMap, githubIssueMap)
    }

    private fun getAllEpics(repos: List<GithubRepo>): List<Epic> {
        return repos.map { repo ->
            api.getEpicsIds(repo.id).mapNotNull { epicId ->
                api.getEpic(repo.id, epicId)
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
            val issues = api.getGithubIssues(owner, repo.name)
            issues.forEach {
                it.repoId = repo.id
                it.repoName = repo.name
            }
            issues
        }.flatten()
    }

    private fun createGithubIssueMap(issues: List<GithubIssue>): Map<Int, Map<Int, GithubIssue>> {
        val map = mutableMapOf<Int, MutableMap<Int, GithubIssue>>()
        issues.forEach { issue ->
            map.putIfAbsent(issue.repoId, mutableMapOf())
            map[issue.repoId]!![issue.number] = issue
        }

        return map
    }

    private fun getAllCards(githubIssues: List<GithubIssue>, zenIssueMap: Map<Int, Map<Int, ZenIssue>>, githubIssueMap: Map<Int, Map<Int, GithubIssue>>): List<Card> {
        return githubIssues.mapNotNull { githubIssue ->
            getCardInfo(githubIssue, zenIssueMap, githubIssueMap)
        }
    }

    private fun getCardInfo(githubIssue: GithubIssue, zenIssueMap: Map<Int, Map<Int, ZenIssue>>, githubIssueMap: Map<Int, Map<Int, GithubIssue>>): Card? {
        val zenIssue = zenIssueMap[githubIssue.repoId]?.get(githubIssue.number)
        val githubEpic = githubIssueMap[githubIssue.repoId]?.get(githubIssue.number)
        val epicTitle = githubEpic?.title ?: "Unknown"

        return createCard(githubIssue, zenIssue, githubIssue.repoName, epicTitle)
    }

    private fun createCard(githubIssue: GithubIssue, zenIssue: ZenIssue?, repoName: String, epicTitle: String): Card? {
        val assignees = githubIssue.assignees.map { it.login }
        val labels = githubIssue.labels.map { it.name }
        val description = cleanNewLines(githubIssue.body)

        return Card(githubIssue.id, repoName, githubIssue.number, githubIssue.title, description, zenIssue?.estimate?.value
                ?: 0, epicTitle, githubIssue.milestone?.title ?: "", zenIssue?.pipeline?.name
                ?: "None", assignees, labels)
    }

    fun printCards(cards: List<Card>) {
        if (cards.isNotEmpty()) {
            fileName = "card_results_${System.currentTimeMillis()}.csv"
            File(fileName).printWriter().use { out ->
                out.println(cards.first().printHeaderRow())
                out.println(cards.joinToString("\n") { it.print() })
            }
//        println(cards.joinToString("\n") {it.print()})
        } else {
            println("No cards to print")
        }
    }


}

fun cleanNewLines(input: String): String {
    return input
            .replace("\n", "\\n")
            .replace("\r", "\\n")
}