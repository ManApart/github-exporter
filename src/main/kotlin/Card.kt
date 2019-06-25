data class Card(val id: Int,
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
        return "Issue ID|Repo Name|Issue Number|Issue Title|Description|Estimate|Epic Title|Milestone Title|Status|Last Update Time|Assignees|Labels"
    }

    fun print(): String {
        return listOf(
                id.toString(),
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

    companion object {
        fun fromCSV(tokens: List<String>): Card {
            val id = Integer.parseInt(tokens[0])
            val repoName = tokens[1]
            val number = Integer.parseInt(tokens[2])
            val title = tokens[3]
            val description = tokens[4]
            val estimate = tokens[5]
            val epicTitle = tokens[6]
            val milestone = tokens[7]
            val lastUpdateTime = tokens[8]
            val status = tokens[9]
            val assignees = tokens[10].split(",")
            val labels = tokens[11].split(",")

            return Card(id, repoName, number, title, description, estimate, epicTitle, milestone, status, lastUpdateTime, assignees, labels)
        }
    }
}
