package api

import Epic
import GithubIssue
import GithubRepo

interface Api {
    fun getEpicsIds(repoId: Int): List<Int>
    fun getEpic(repoId: Int, epicId: Int): Epic?
    fun getGithubRepos(owner: String) : List<GithubRepo>
    fun getGithubIssue(owner: String, repoName: String, issueId: Int) : GithubIssue?
}