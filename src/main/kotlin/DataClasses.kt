import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class EpicIssues(val epic_issues: List<Epic>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Epic(val issue_number: Int, val repo_id: Int, val issue_url: String?, val issues: List<ZenIssue> = listOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class ZenIssue(val issue_number: Int, val repo_id: Int, val estimate: Estimate?, val is_epic: Boolean)

data class Estimate(val value: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubIssue(val id: Int, val number: Int, val title: String, val labels: List<Label>, val body: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Label(val id: Int, val name: String, val color: String)