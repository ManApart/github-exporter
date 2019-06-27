package api

import Epic
import EpicIssues
import GithubIssue
import GithubRepo
import Params
import Links
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream
import java.net.URL

const val MAX_RETRIES = 5
const val RETRY_DELAY = 1000

class LiveApi(private val params: Params) : Api {

    override fun getEpicsIds(repoId: Int, retries: Int): List<Int> {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics?access_token=${params.zenhubToken}"

        return try {
            val response = makeCall(url)
            val issues: EpicIssues = jacksonObjectMapper().readValue(response)

            issues.epic_issues.map { it.issue_number }
        } catch (e: Exception) {
            if (retries < MAX_RETRIES) {
                Thread.sleep((retries * RETRY_DELAY).toLong())
                getEpicsIds(repoId, retries + 1)
            } else {
                println("Couldn't fetch Epic Ids for repo $repoId")
                listOf()
            }
        }
    }

    override fun getEpic(repoId: Int, epicId: Int, retries: Int): Epic? {
        val url = "https://api.zenhub.io/p1/repositories/$repoId/epics/$epicId?access_token=${params.zenhubToken}"
        return try {
            val response = makeCall(url)
            jacksonObjectMapper().readValue(response)
        } catch (e: Exception) {
            if (retries < MAX_RETRIES) {
                Thread.sleep((retries * RETRY_DELAY).toLong())
                getEpic(repoId, epicId, retries + 1)
            } else {
                println("Couldn't fetch Epic $epicId for repo $repoId")
                null
            }
        }
    }

    override fun getGithubReposByOrg(org: String): List<GithubRepo> {
        val url = "https://api.github.com/orgs/$org/repos"
        return try {
            return makeRepoCalls(url)
        } catch (e: Exception) {
            println("Couldn't fetch Github Repos for organization $org")
            listOf()
        }
    }

    override fun getGithubReposByUser(user: String): List<GithubRepo> {
        val url = "https://api.github.com/users/$user/repos"
        return try {
            return makeRepoCalls(url)
        } catch (e: Exception) {
            println("Couldn't fetch Github Repos for user $user")
            listOf()
        }
    }

    private fun makeRepoCalls(url: String): List<GithubRepo> {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${params.githubToken}")
        val response = connection.getInputStream()

        val links = Links(connection.getHeaderField("Link"))
        val nextLink = links.links.firstOrNull { it.rel == "next" }
        return if (nextLink != null) {
            val repos = mutableListOf<GithubRepo>()
            repos.addAll(jacksonObjectMapper().readValue(response))
            repos.addAll(makeRepoCalls(nextLink.url))
            repos
        } else {
            jacksonObjectMapper().readValue(response)
        }
    }

    override fun getGithubIssues(owner: String, repoName: String): List<GithubIssue> {
        val url = "https://api.github.com/repos/$owner/$repoName/issues?state=all"
        return try {
            makeIssueCalls(url)
        } catch (e: Exception) {
            println("Couldn't fetch Github Issues for $repoName")
            listOf()
        }
    }

    private fun makeIssueCalls(url: String): List<GithubIssue> {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${params.githubToken}")
        val response = connection.getInputStream()

        val links = Links(connection.getHeaderField("Link"))
        val nextLink = links.links.firstOrNull { it.rel == "next" }
        return if (nextLink != null) {
            val issues = mutableListOf<GithubIssue>()
            issues.addAll(jacksonObjectMapper().readValue(response))
            issues.addAll(makeIssueCalls(nextLink.url))
            issues
        } else {
            jacksonObjectMapper().readValue(response)
        }
    }

    override fun getGithubIssue(owner: String, repoName: String, issueNumber: Int): GithubIssue? {
        val url = "https://api.github.com/repos/$owner/$repoName/issues/$issueNumber"
        return try {
            val response = makeCall(url)
            jacksonObjectMapper().readValue(response)
        } catch (e: Exception) {
            println("Couldn't fetch Github Issues for $repoName $issueNumber")
            null
        }
    }

    private fun makeCall(url: String): InputStream {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${params.githubToken}")
        connection.headerFields
        return connection.getInputStream()
    }

}