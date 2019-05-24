import api.Api

class CardPrinter(private val api: Api) {

    fun getCards() : List<Card> {
        return api.getEpicsIds()
                .map { getCards(it) }
                .flatten()
    }

    fun printCards(cards: List<Card>) {
        println(cards.joinToString("\n") {it.print()})
    }

    private fun getCards(epicId: Int): List<Card> {
        val epic = api.getEpic(epicId)
        val epicTitle = api.getGithubIssue(epicId).title
        return epic.issues.map { createCard(it, epicTitle) }
    }

    private fun createCard(issue: ZenIssue, epicTitle: String): Card {
        val githubIssue = api.getGithubIssue(issue.issue_number)

        return Card(githubIssue.id, githubIssue.number, githubIssue.title, githubIssue.body, issue.estimate?.value ?: 0, epicTitle, githubIssue.labels)
    }

}