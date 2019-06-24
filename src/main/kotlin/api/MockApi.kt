package api

import Epic
import Estimate
import GithubIssue
import GithubRepo
import GithubUser
import Milestone
import Pipeline
import ZenIssue

//Make private vals match override, default function provide private vals
class MockApi(
        private val epicIds: Map<Int, List<Int>> = defaultEpicIds(),
        private val epics: Map<Int, Map<Int, Epic>> = defaultEpics(),
        private val githubRepos: Map<String, List<GithubRepo>> = defaultGithubRepos(),
        private val githubIssues: Map<String, Map<String, List<GithubIssue>>> = defaultGithubIssues()
) : Api {

    override fun getEpicsIds(repoId: Int): List<Int> {
        return epicIds.getValue(repoId)
    }

    override fun getEpic(repoId: Int, epicId: Int): Epic? {
        return epics.getValue(repoId).getValue(epicId)
    }

    override fun getGithubRepos(owner: String): List<GithubRepo> {
        return githubRepos.getValue(owner)
    }

    override fun getGithubIssues(owner: String, repoName: String): List<GithubIssue> {
        return githubIssues.getValue(owner).getValue(repoName)
    }
}

private fun defaultEpicIds(): Map<Int, List<Int>> {
    return mapOf(55 to listOf(1, 2, 3),
            56 to listOf(4, 5, 6))
}

private fun defaultEpics(): Map<Int, Map<Int, Epic>> {
    return mapOf(
            55 to mapOf(
                    1 to Epic(1 * 10, 55, "issue_url", defaultIssues(55, 1 * 10)),
                    2 to Epic(2 * 10, 55, "issue_url2", defaultIssues(55, 2 * 10)),
                    3 to Epic(2 * 10, 55, "issue_url3", defaultIssues(55, 3 * 10))),
            56 to mapOf(
                    4 to Epic(4 * 10, 56, "issue_url", defaultIssues(56, 4 * 10)),
                    5 to Epic(5 * 10, 56, "issue_url2", defaultIssues(56, 5 * 10)),
                    6 to Epic(6 * 10, 56, "issue_url3", defaultIssues(56, 6 * 10))
            ))
}

private fun defaultIssues(repoId: Int, epicId: Int): List<ZenIssue> {
    return listOf(
            ZenIssue(epicId + 1, repoId, Estimate(1), false, Pipeline("id", "pipeline")),
            ZenIssue(epicId + 2, repoId, Estimate(2), false, Pipeline("id", "pipeline")),
            ZenIssue(epicId + 3, repoId, Estimate(3), false, Pipeline("id", "pipeline"))
    )
}

private fun defaultGithubRepos(): Map<String, List<GithubRepo>> {
    return mapOf("owner" to listOf(
            GithubRepo(55, "repoA", "A repo", GithubUser(1, "owner")),
            GithubRepo(56, "repoB", "Another repo", GithubUser(1, "owner"))
    ))
}

private fun defaultGithubIssues(): Map<String, Map<String, List<GithubIssue>>> {
    return mapOf(
            "owner" to
                    mapOf(
                            "repoA" to listOf(
                                    defaultGithubIssueForEpic(1),
                                    defaultGithubIssueForEpic(10),
                                    defaultGithubIssueForEpic(11),
                                    defaultGithubIssueForEpic(12),

                                    defaultGithubIssueForEpic(2),
                                    defaultGithubIssueForEpic(21),
                                    defaultGithubIssueForEpic(22),
                                    defaultGithubIssueForEpic(23),

                                    defaultGithubIssueForEpic(3),
                                    defaultGithubIssueForEpic(31),
                                    defaultGithubIssueForEpic(32),
                                    defaultGithubIssueForEpic(33)
                            ),
                            "repoB" to listOf(
                                    defaultGithubIssueForEpic(4),
                                    defaultGithubIssueForEpic(40),
                                    defaultGithubIssueForEpic(41),
                                    defaultGithubIssueForEpic(42),

                                    defaultGithubIssueForEpic(5),
                                    defaultGithubIssueForEpic(51),
                                    defaultGithubIssueForEpic(52),
                                    defaultGithubIssueForEpic(53),

                                    defaultGithubIssueForEpic(6),
                                    defaultGithubIssueForEpic(61),
                                    defaultGithubIssueForEpic(62),
                                    defaultGithubIssueForEpic(63)
                            )
                    )
    )
}

private fun defaultGithubIssueForEpic(issueId: Int): GithubIssue {
    return GithubIssue(issueId, issueId, "This is a title for $issueId", listOf(), "This is a description for $issueId", Milestone(123, "milestone"), listOf())
}