package api

import Epic
import EpicIssues
import GithubIssue
import GithubRepo
import Params
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL


class LiveApi(private val params: Params) : Api {

    override fun getEpicsIds(repoId: Int): List<Int> {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics?access_token=${params.token}"
        val response = URL(url).readText()

        val issues: EpicIssues = jacksonObjectMapper().readValue(response)
        return issues.epic_issues.map { it.issue_number }
    }

    override fun getEpic(repoId: Int, epicId: Int): Epic {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics/$epicId?access_token=${params.token}"
        val response = URL(url).readText()

        return jacksonObjectMapper().readValue(response)
    }

    override fun getGithubRepos(owner: String): List<GithubRepo> {
        val url = "https://api.github.com/users/$owner/repos"
        val response = URL(url).readText()

        return jacksonObjectMapper().readValue(response)
    }

    override fun getGithubIssue(owner: String, repoName: String, issueId: Int) : GithubIssue {
        val url = "https://api.github.com/repos/$owner/$repoName/issues/$issueId"
        val response = URL(url).readText()

        return jacksonObjectMapper().readValue(response)
    }

}