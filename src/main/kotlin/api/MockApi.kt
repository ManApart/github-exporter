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
        private val githubIssues: Map<String, Map<String, Map<Int, GithubIssue>>> = defaultGithubIssues()
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

    override fun getGithubIssue(owner: String, repoName: String, issueId: Int): GithubIssue? {
        return githubIssues.getValue(owner).getValue(repoName).getValue(issueId)
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

private fun defaultGithubIssues(): Map<String, Map<String, Map<Int, GithubIssue>>> {
    return mapOf(
            "owner" to
                    mapOf(
                            "repoA" to mapOf(
                                    1 to defaultGithubIssueForEpic(1),
                                    11 to defaultGithubIssueForEpic(10),
                                    12 to defaultGithubIssueForEpic(11),
                                    13 to defaultGithubIssueForEpic(12),

                                    2 to defaultGithubIssueForEpic(2),
                                    21 to defaultGithubIssueForEpic(21),
                                    22 to defaultGithubIssueForEpic(22),
                                    23 to defaultGithubIssueForEpic(23),

                                    3 to defaultGithubIssueForEpic(3),
                                    31 to defaultGithubIssueForEpic(31),
                                    32 to defaultGithubIssueForEpic(32),
                                    33 to defaultGithubIssueForEpic(33)
                            ),
                            "repoB" to mapOf(
                                    4 to defaultGithubIssueForEpic(4),
                                    41 to defaultGithubIssueForEpic(40),
                                    42 to defaultGithubIssueForEpic(41),
                                    43 to defaultGithubIssueForEpic(42),

                                    5 to defaultGithubIssueForEpic(5),
                                    51 to defaultGithubIssueForEpic(51),
                                    52 to defaultGithubIssueForEpic(52),
                                    53 to defaultGithubIssueForEpic(53),

                                    6 to defaultGithubIssueForEpic(6),
                                    61 to defaultGithubIssueForEpic(61),
                                    62 to defaultGithubIssueForEpic(62),
                                    63 to defaultGithubIssueForEpic(63)
                            )
                    )
    )
}

private fun defaultGithubIssueForEpic(issueId: Int): GithubIssue {
    return GithubIssue(issueId, issueId, "This is a title for $issueId", listOf(), "This is a description for $issueId", Milestone(123, "milestone"), listOf())
}