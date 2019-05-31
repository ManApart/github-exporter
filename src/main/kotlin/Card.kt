data class Card(val id: Int,
                val repoName: String,
                val number: Int,
                val title: String,
                val description: String,
                val estimate: Int,
                val epicTitle: String,
                val milestone: String,
                val status: String,
                val assignees: List<String>,
                val labels: List<String>
) {

    //Card could have multiple epics
    fun printHeaderRow(): String {
        return "Issue ID|Repo Name|Issue Number|Issue Title|Description|Estimate|Epic Title|Milestone Title|Status|Assignees|Labels"
    }

    fun print(): String {
        return listOf(
                id.toString(),
                repoName,
                number.toString(),
                title,
                description,
                estimate.toString(),
                epicTitle,
                milestone,
                status,
                assignees.joinToString(","),
                labels.joinToString(",")

        )
                .joinToString("|")
    }


}