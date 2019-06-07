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
        ).joinToString("|")
    }

    companion object {
        fun fromCSV(tokens: List<String>): Card {
            val id = Integer.parseInt(tokens[0])
            val repoName = tokens[1]
            val number = Integer.parseInt(tokens[2])
            val title = tokens[3]
            val description = tokens[4]
            val estimate = Integer.parseInt(tokens[5])
            val epicTitle = tokens[6]
            val milestone = tokens[7]
            val status = tokens[8]
            val assignees = tokens[9].split(",")
            val labels = tokens[10].split(",")

            return Card(id, repoName, number, title, description, estimate, epicTitle, milestone, status, assignees, labels)
        }
    }
}
