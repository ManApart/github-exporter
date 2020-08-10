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
        private val epicIds: Map<String, List<String>> = defaultEpicIds(),
        private val epics: Map<String, Map<String, Epic>> = defaultEpics(),
        private val githubRepos: Map<String, List<GithubRepo>> = defaultGithubRepos(),
        private val githubIssues: Map<String, Map<String, List<GithubIssue>>> = defaultGithubIssues()
) : Api {


    override fun getEpicsIds(repoId: String, retries: Int): List<String> {
        return epicIds.getValue(repoId)
    }

    override fun getEpic(repoId: String, epicId: String, retries: Int): Epic? {
        return epics.getValue(repoId).getValue(epicId)
    }

    override fun getGithubReposByOrg(org: String): List<GithubRepo> {
        return githubRepos.getValue(org)
    }

    override fun getGithubReposByUser(user: String): List<GithubRepo> {
        return githubRepos.getValue(user)
    }

    override fun getGithubIssues(owner: String, repoName: String): List<GithubIssue> {
        return githubIssues.getValue(owner).getValue(repoName)
    }

    override fun getGithubIssue(owner: String, repoName: String, issueNumber: String): GithubIssue? {
        return githubIssues.getValue(owner).getValue(repoName).firstOrNull { it.number == issueNumber }
    }

}

private fun defaultEpicIds(): Map<String, List<String>> {
    return mapOf("55" to listOf("1","2", "3"),
            "56" to listOf("4", "5", "6"))
}

private fun defaultEpics(): Map<String, Map<String, Epic>> {
    return mapOf(
            "55" to mapOf(
                    "1" to Epic("10", "55", "issue_url", defaultIssues("55", "10")),
                    "2" to Epic("20", "55", "issue_url2", defaultIssues("55", "20")),
                    "3" to Epic("20", "55", "issue_url3", defaultIssues("55", "30"))),
            "56" to mapOf(
                    "4" to Epic("40", "56", "issue_url", defaultIssues("56", "40")),
                    "5" to Epic("50", "56", "issue_url2", defaultIssues("56", "50")),
                    "6" to Epic("60", "56", "issue_url3", defaultIssues("56", "60"))
            ))
}

private fun defaultIssues(repoId: String, epicId: String): List<ZenIssue> {
    val epicInt = epicId.toInt()
    return listOf(
            ZenIssue((epicInt + 1).toString(), repoId, Estimate(1), false, Pipeline("id", "pipeline")),
            ZenIssue((epicInt + 2).toString(), repoId, Estimate(2), false, Pipeline("id", "pipeline")),
            ZenIssue((epicInt + 3).toString(), repoId, Estimate(3), false, Pipeline("id", "pipeline"))
    )
}

private fun defaultGithubRepos(): Map<String, List<GithubRepo>> {
    return mapOf("owner" to listOf(
            GithubRepo("55", "repoA", "A repo", GithubUser("1", "owner")),
            GithubRepo("56", "repoB", "Another repo", GithubUser("1", "owner"))
    ))
}

private fun defaultGithubIssues(): Map<String, Map<String, List<GithubIssue>>> {
    return mapOf(
            "owner" to
                    mapOf(
                            "repoA" to listOf(
                                    defaultGithubIssueForEpic("1"),
                                    defaultGithubIssueForEpic("10"),
                                    defaultGithubIssueForEpic("11"),
                                    defaultGithubIssueForEpic("12"),
                                    defaultGithubIssueForEpic("2"),
                                    defaultGithubIssueForEpic("21"),
                                    defaultGithubIssueForEpic("22"),
                                    defaultGithubIssueForEpic("23"),
                                    defaultGithubIssueForEpic("3"),
                                    defaultGithubIssueForEpic("31"),
                                    defaultGithubIssueForEpic("32"),
                                    defaultGithubIssueForEpic("33")
                            ),
                            "repoB" to listOf(
                                    defaultGithubIssueForEpic("4"),
                                    defaultGithubIssueForEpic("40"),
                                    defaultGithubIssueForEpic("41"),
                                    defaultGithubIssueForEpic("42"),
                                    defaultGithubIssueForEpic("5"),
                                    defaultGithubIssueForEpic("51"),
                                    defaultGithubIssueForEpic("52"),
                                    defaultGithubIssueForEpic("53"),
                                    defaultGithubIssueForEpic("6"),
                                    defaultGithubIssueForEpic("61"),
                                    defaultGithubIssueForEpic("62"),
                                    defaultGithubIssueForEpic("63")
                            )
                    )
    )
}

private fun defaultGithubIssueForEpic(issueId: String): GithubIssue {
    return GithubIssue(issueId, issueId, "This is a title for $issueId", listOf(), "This is a description for $issueId", Milestone("123", "milestone"), "updated at", null, listOf())
}