package api

import Epic
import GithubIssue
import ZenIssue
import Estimate

class MockApi(
        private val epicIds: List<Int> = defaultEpicIds(),
        private val epics: Map<Int, Epic> = defaultEpics(5),
        private val githubIssues: Map<Int, GithubIssue> = defaultGithubIssues(5)
) : Api {

    override fun getEpicsIds(): List<Int> {
        return epicIds
    }

    override fun getEpic(id: Int): Epic {
        return epics.getValue(id)
    }

    override fun getGithubIssue(id: Int): GithubIssue {
        return githubIssues.getValue(id)
    }
}

fun defaultEpicIds() : List<Int> {
    return listOf(1,2,3)
}

fun defaultEpics(repoId: Int) : Map<Int, Epic> {
    return mapOf(
            1 to Epic(1*10, repoId, "issue_url", defaultIssues(repoId, 1*10)),
            2 to Epic(2*10, repoId, "issue_url2", defaultIssues(repoId, 2*10)),
            3 to Epic(2*10, repoId, "issue_url3", defaultIssues(repoId, 3*10))
    )
}

fun defaultIssues(repoId: Int, epicId: Int) : List<ZenIssue> {
    return listOf(
            ZenIssue(epicId+1, repoId, Estimate(1), false),
            ZenIssue(epicId+2, repoId, Estimate(2), false),
            ZenIssue(epicId+3, repoId, Estimate(3), false)
    )
}

fun defaultGithubIssues(repoId: Int) : Map<Int, GithubIssue> {
    return mapOf(
            1 to defaultGithubIssueForEpic(repoId, 1),
            11 to defaultGithubIssueForEpic(repoId, 10),
            12 to defaultGithubIssueForEpic(repoId, 11),
            13 to defaultGithubIssueForEpic(repoId, 12),

            2 to defaultGithubIssueForEpic(repoId, 2),
            21 to defaultGithubIssueForEpic(repoId, 21),
            22 to defaultGithubIssueForEpic(repoId, 22),
            23 to defaultGithubIssueForEpic(repoId, 23),

            3 to defaultGithubIssueForEpic(repoId, 3),
            31 to defaultGithubIssueForEpic(repoId, 31),
            32 to defaultGithubIssueForEpic(repoId, 32),
            33 to defaultGithubIssueForEpic(repoId, 33)
    )
}

private fun defaultGithubIssueForEpic(repoId: Int, issueId: Int) : GithubIssue {
    return GithubIssue(issueId, issueId, "This is a title for $issueId", listOf(), "This is a description for $issueId")
}