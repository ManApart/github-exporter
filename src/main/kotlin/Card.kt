data class Card(val id: Int,
                val organization: String,
                val repoName: String,
                val number: Int,
                val title: String,
                val description: String,
                val estimate: String,
                val epicTitle: String,
                val milestone: String,
                val status: String,
                val lastUpdateTime: String,
                val assignees: List<String>,
                val labels: List<String>
) {

    fun printHeaderRow(): String {
        return "Issue ID|Organization|Repo Name|Issue Number|Issue Title|Description|Estimate|Epic Title|Milestone Title|Status|Last Update Time|Assignees|Labels"
    }

    fun print(): String {
        return listOf(
                id.toString(),
                organization,
                repoName,
                number.toString(),
                title,
                description,
                estimate,
                epicTitle,
                milestone,
                status,
                lastUpdateTime,
                assignees.joinToString(","),
                labels.joinToString(",")
        ).joinToString("|")
    }
}
