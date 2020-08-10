data class Card(val id: String,
                val organization: String,
                val repoName: String,
                val number: String,
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
                makePipeSafe(title),
                makePipeSafe(description),
                estimate,
                makePipeSafe(epicTitle),
                milestone,
                status,
                lastUpdateTime,
                assignees.joinToString(","),
                labels.joinToString(",")
        ).joinToString("|")
    }

    fun makePipeSafe(value: String): String {
        return value.replace("|", "[")
    }
}
