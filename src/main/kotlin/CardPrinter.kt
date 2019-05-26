import api.Api
import java.io.File

class CardPrinter(private val api: Api) {

    fun getCards(owner: String): List<Card> {
        return api.getGithubRepos(owner).map { repo ->
            println("Getting info for repo ${repo.name}")
            api.getEpicsIds(repo.id).map { epicId ->
                getCards(repo, epicId)
            }.flatten()
        }.flatten()
    }

    fun printCards(cards: List<Card>) {
        val fileName = "card_results_${System.currentTimeMillis()}.csv"
        File(fileName).printWriter().use { out ->
            out.println(cards.joinToString("\n") { it.print() })
        }
//        println(cards.joinToString("\n") {it.print()})
    }

    private fun getCards(repo: GithubRepo, epicId: Int): List<Card> {
        val epic = api.getEpic(repo.id, epicId)
        val epicTitle = api.getGithubIssue(repo.owner.login, repo.name, epicId).title
        println("Getting info for epic $epicTitle")
        return epic.issues.map {
            createCard(it, repo.owner.login, repo.name, epicTitle)
        }
    }

    private fun createCard(issue: ZenIssue, repoOwner: String, repoName: String, epicTitle: String): Card {
        val githubIssue = api.getGithubIssue(repoOwner, repoName, issue.issue_number)

        return Card(githubIssue.id, githubIssue.number, githubIssue.title, githubIssue.body, issue.estimate?.value
                ?: 0, epicTitle, githubIssue.labels)
    }

}