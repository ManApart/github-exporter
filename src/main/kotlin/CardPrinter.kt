import api.Api
import java.io.File

class CardPrinter(private val api: Api, private val previousCards: Map<String, Map<Int, Card>>) {
    var cardsPreserved = 0
    var cardsFetched = 0


    fun getCards(owner: String): List<Card> {
        return api.getGithubRepos(owner).map { repo ->
            println("Getting info for repo ${repo.name}")
            api.getEpicsIds(repo.id).map { epicId ->
                getCards(repo, epicId)
            }.flatten()
        }.flatten()
    }

    fun printCards(cards: List<Card>) {
        if (cards.isNotEmpty()) {
            val fileName = "card_results_${System.currentTimeMillis()}.csv"
            File(fileName).printWriter().use { out ->
                out.println(cards.first().printHeaderRow())
                out.println(cards.joinToString("\n") { it.print() })
            }
//        println(cards.joinToString("\n") {it.print()})
        } else {
            println("No cards to print")
        }
    }

    private fun getCards(repo: GithubRepo, epicId: Int): List<Card> {
        val epic = api.getEpic(repo.id, epicId)
        val epicTitle = api.getGithubIssue(repo.owner.login, repo.name, epicId)?.title ?: "Unknown"
        return if (epic != null) {
            println("Getting info for epic $epicTitle")
            epic.issues.mapNotNull {
                getLatestCardInfo(it, repo.owner.login, repo.name, epicTitle)
            }
        } else {
            listOf()
        }
    }

    private fun getLatestCardInfo(issue: ZenIssue, repoOwner: String, repoName: String, epicTitle: String): Card? {
        val previousCard = previousCards[repoName]?.get(issue.issue_number)
        return if (previousCard != null && isUpToDate(previousCard, issue)) {
            cardsPreserved++
            previousCard
        } else {
            cardsFetched++
            createCard(issue, repoOwner, repoName, epicTitle)
        }
    }

    private fun isUpToDate(card: Card, issue: ZenIssue): Boolean {
        return card.status.toLowerCase() == "closed" && "closed" == issue.pipeline?.name?.toLowerCase()
    }

    private fun createCard(issue: ZenIssue, repoOwner: String, repoName: String, epicTitle: String): Card? {
        val githubIssue = api.getGithubIssue(repoOwner, repoName, issue.issue_number)
        return if (githubIssue != null) {
            val assignees = githubIssue.assignees.map { it.login }
            val labels = githubIssue.labels.map { it.name }
            val description = cleanNewLines(githubIssue.body)

            Card(githubIssue.id, repoName, githubIssue.number, githubIssue.title, description, issue.estimate?.value
                    ?: 0, epicTitle, githubIssue.milestone?.title ?: "", issue.pipeline?.name
                    ?: "None", assignees, labels)
        } else {
            null
        }
    }

}

fun cleanNewLines(input: String): String {
    return input
            .replace("\n", "\\n")
            .replace("\r", "\\n")
}