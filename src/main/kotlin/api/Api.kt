package api

import Epic
import GithubIssue
import GithubRepo

interface Api {
    fun getEpicsIds(repoId: Int, retries: Int): List<Int>
    fun getEpic(repoId: Int, epicId: Int, retries: Int): Epic?
    fun getGithubReposByOrg(org: String) : List<GithubRepo>
    fun getGithubReposByUser(user: String) : List<GithubRepo>
    fun getGithubIssues(owner: String, repoName: String) : List<GithubIssue>
    fun getGithubIssue(owner: String, repoName: String, issueNumber: Int) : GithubIssue?
}