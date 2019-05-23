import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

//Pull hardcoded variables out
//Allow variable pass in

fun main(args: Array<String>) {
    val params = Params(REPO_OWNER, REPO_ID, REPO_NAME, TOKEN)

    val cards = getEpicsIds(params).map { getCards(it, params) }.flatten()
    println(cards.joinToString(" "))
}


private fun getEpicsIds(params: Params): List<Int> {
    val url = "https://api.zenhub.io/p1/repositories/${params.repoId}/epics?access_token=${params.token}"
    val response = URL(url).readText()

    val issues: EpicIssues = jacksonObjectMapper().readValue(response)
    return issues.epic_issues.map { it.issue_number }
}

private fun getCards(id: Int, params: Params): List<Card> {
    val epic = getEpic(id, params)
    val epicTitle = getGithubIssue(id, params).title
    return epic.issues.map { createCard(it, epicTitle, params) }
}

private fun getEpic(id: Int, params: Params): Epic {
    val url = "https://api.zenhub.io/p1/repositories/${params.repoId}/epics/$id?access_token=${params.token}"
    val response = URL(url).readText()

    return jacksonObjectMapper().readValue(response)
}

private fun createCard(issue: ZenIssue, epicTitle: String, params: Params): Card {
    val githubIssue = getGithubIssue(issue.issue_number, params)

    return Card(githubIssue.id, githubIssue.number, githubIssue.title, githubIssue.body, issue.estimate?.value ?: 0, epicTitle, githubIssue.labels)
}

private fun getGithubIssue(id: Int, params: Params) : GithubIssue {
    val url = "https://api.github.com/repos/${params.owner}/${params.repoName}/issues/$id"
    val response = URL(url).readText()

    return jacksonObjectMapper().readValue(response)
}

