package api

import Epic
import GithubIssue
import GithubRepo

interface Api {
    fun getEpicsIds(repoId: String, retries: Int): List<String>
    fun getEpic(repoId: String, epicId: String, retries: Int): Epic?
    fun getGithubReposByOrg(org: String) : List<GithubRepo>
    fun getGithubReposByUser(user: String) : List<GithubRepo>
    fun getGithubIssues(owner: String, repoName: String) : List<GithubIssue>
    fun getGithubIssue(owner: String, repoName: String, issueNumber: String) : GithubIssue?
}