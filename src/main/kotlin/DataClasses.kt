import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubRepo(val id: Int, val name: String, val description: String? = "", val owner: GithubUser)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubUser(val id: Int, val login: String)

data class EpicIssues(val epic_issues: List<Epic>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Epic(val issue_number: Int, val repo_id: Int, val issue_url: String?, val issues: List<ZenIssue> = listOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class ZenIssue(val issue_number: Int, val repo_id: Int, val estimate: Estimate?, val is_epic: Boolean, val pipeline: Pipeline?)

data class Estimate(val value: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Pipeline(val pipeline_id: String = "", val name: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubIssue(val id: Int, val number: Int, val title: String, val labels: List<Label>, val body: String, val milestone: Milestone?, val assignees: List<GithubUser> = listOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class Milestone(val id: Int, val title: String = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class Label(val id: Int, val name: String, val color: String)