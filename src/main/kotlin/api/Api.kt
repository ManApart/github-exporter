package api

import Epic
import GithubIssue

interface Api {
    fun getEpicsIds(): List<Int>
    fun getEpic(id: Int): Epic
    fun getGithubIssue(id: Int) : GithubIssue
}