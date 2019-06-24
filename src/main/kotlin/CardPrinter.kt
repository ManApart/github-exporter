import api.Api
import java.io.File

class CardPrinter(private val api: Api) {
    var fileName = "unknown"


    fun getCards(owner: String): List<Card> {
        println("Finding cards for $owner")
        val repos = api.getGithubRepos(owner)
        println("Found ${repos.size} repos")
        val epics = getAllEpics(repos)
        println("Found ${epics.size} epics")
        val githubIssues = getAllGithubIssues(owner, repos)
        println("Found ${githubIssues.size} issues")
        return getAllCards(githubIssues, epics)
    }

    private fun getAllEpics(repos: List<GithubRepo>): List<Epic> {
        return repos.map { repo ->
            api.getEpicsIds(repo.id).mapNotNull { epicId ->
                api.getEpic(repo.id, epicId)
            }
        }.flatten()
    }

    private fun getAllGithubIssues(owner: String, repos: List<GithubRepo>): List<GithubIssue> {
        return repos.map { repo ->
            val issues = api.getGithubIssues(owner, repo.name)
            issues.forEach {
                it.repo_id = repo.id
                it.repoName = repo.name
            }
            issues
        }.flatten()
    }

    private fun getAllCards(githubIssues: List<GithubIssue>, epics: List<Epic>): List<Card> {
        return githubIssues.mapNotNull { githubIssue ->
            getCardInfo(githubIssue, epics, githubIssues)
        }
    }

    private fun getCardInfo(githubIssue: GithubIssue, epics: List<Epic>, githubIssues: List<GithubIssue>): Card? {
        val epic = findEpic(githubIssue, epics)
        val zenIssue = findZenIssue(epic, githubIssue)
        val githubEpic = findGithubIssue(epic, githubIssues)
        val epicTitle = githubEpic?.title ?: "Unknown"

        return createCard(githubIssue, zenIssue, githubIssue.repoName, epicTitle)
    }

    private fun findEpic(githubIssue: GithubIssue, epics: List<Epic>): Epic? {
        return epics.firstOrNull { epic ->
            epic.issues.any {
                matches(githubIssue, it)
            }
        }
    }

    private fun findZenIssue(epic: Epic?, githubIssue: GithubIssue): ZenIssue? {
        return epic?.issues?.firstOrNull { zenIssue ->
            matches(githubIssue, zenIssue)
        }
    }

    private fun findGithubIssue(epic: Epic?, githubIssues: List<GithubIssue>): GithubIssue? {
        return if (epic == null) {
            null
        } else {
            githubIssues.firstOrNull { githubIssue ->
                epic.issues.any { zenIssue ->
                    matches(githubIssue, zenIssue)
                }
            }
        }
    }

    private fun matches(githubIssue: GithubIssue, zenIssue: ZenIssue): Boolean {
        return zenIssue.repo_id == githubIssue.repo_id && zenIssue.issue_number == githubIssue.number
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