package api

import Epic
import EpicIssues
import GithubIssue
import GithubRepo
import Params
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream
import java.net.URL


class LiveApi(private val params: Params) : Api {

    override fun getEpicsIds(repoId: Int): List<Int> {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics?access_token=${params.zenhubToken}"

        return try {
            val response = makeCall(url)
            val issues: EpicIssues = jacksonObjectMapper().readValue(response)

            issues.epic_issues.map { it.issue_number }
        } catch (e: Exception) {
            println("Couldn't fetch Epic Ids for repo $repoId")
            listOf()
        }
    }

    override fun getEpic(repoId: Int, epicId: Int): Epic? {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics/$epicId?access_token=${params.zenhubToken}"
        return try {
            val response = makeCall(url)
            jacksonObjectMapper().readValue(response)
        } catch (e: Exception) {
            println("Couldn't fetch Epic $epicId for repo $repoId")
            null
        }
    }

    override fun getGithubRepos(owner: String): List<GithubRepo> {
        val url = "https://api.github.com/users/$owner/repos"
        return try {
            val response = makeCall(url)
            jacksonObjectMapper().readValue(response)
        } catch (e: Exception) {
            println("Couldn't fetch Github Repos for $owner")
            listOf()
        }
    }

    override fun getGithubIssue(owner: String, repoName: String, issueId: Int): GithubIssue? {
        val url = "https://api.github.com/repos/$owner/$repoName/issues/$issueId"
        return try {
            val response = makeCall(url)
            jacksonObjectMapper().readValue(response)
        } catch (e: Exception) {
            println("Couldn't fetch Github Issue $issueId for $repoName")
            null
        }
    }

    private fun makeCall(url: String): InputStream {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${params.githubToken}")
        return connection.getInputStream()
    }

}