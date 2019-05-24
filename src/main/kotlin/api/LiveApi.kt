package api

import Epic
import EpicIssues
import GithubIssue
import Params
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL


class LiveApi(private val params: Params) : Api {
    override fun getEpicsIds(): List<Int> {
        val url = "https://api.zenhub.io/p1/repositories/${params.repoId}/epics?access_token=${params.token}"
        val response = URL(url).readText()

        val issues: EpicIssues = jacksonObjectMapper().readValue(response)
        return issues.epic_issues.map { it.issue_number }
    }

    override fun getEpic(id: Int): Epic {
        val url = "https://api.zenhub.io/p1/repositories/${params.repoId}/epics/$id?access_token=${params.token}"
        val response = URL(url).readText()

        return jacksonObjectMapper().readValue(response)
    }

    override fun getGithubIssue(id: Int) : GithubIssue {
        val url = "https://api.github.com/repos/${params.owner}/${params.repoName}/issues/$id"
        val response = URL(url).readText()

        return jacksonObjectMapper().readValue(response)
    }

}