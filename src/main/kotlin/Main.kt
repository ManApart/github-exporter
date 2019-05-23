import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

//Pull hardcoded variables to main function
//Pull hardcoded variables out
//Allow variable pass in

fun main(args: Array<String>) {
    val cards = getEpicsIds().map { getCards(it) }.flatten()
    println(cards.joinToString(" "))
}


private fun getEpicsIds(): List<Int> {
    val url = "https://api.zenhub.io/p1/repositories/$REPO_ID/epics?access_token=$TOKEN"
    val response = URL(url).readText()

    val issues: EpicIssues = jacksonObjectMapper().readValue(response)
    return issues.epic_issues.map { it.issue_number }
}

private fun getCards(id: Int): List<Card> {
    val epic = getEpic(id)
    val epicTitle = getGithubIssue(id).title
    return epic.issues.map { createCard(it, epicTitle) }
}

private fun getEpic(id: Int): Epic {
    val url = "https://api.zenhub.io/p1/repositories/$REPO_ID/epics/$id?access_token=$TOKEN"
    val response = URL(url).readText()

    return jacksonObjectMapper().readValue(response)
}

private fun createCard(issue: ZenIssue, epicTitle: String): Card {
    val githubIssue = getGithubIssue(issue.issue_number)

    return Card(githubIssue.id, githubIssue.number, githubIssue.title, githubIssue.body, issue.estimate?.value ?: 0, epicTitle, githubIssue.labels)
}

private fun getGithubIssue(id: Int) : GithubIssue {
    val url = "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/issues/$id"
    val response = URL(url).readText()

    return jacksonObjectMapper().readValue(response)
}

