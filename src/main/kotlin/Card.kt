data class Card(val id: Int, val number: Int, val title: String, val description: String, val estimate: Int, val epicTitle: String, val labels: List<Label>) {

    fun print() : String {
        return listOf(id.toString(), number.toString(), title, description, estimate.toString(), epicTitle, labels.joinToString(","))
                .joinToString("|")
                .dropLast(1)
    }

    fun printHeaderRow() : String {
        return "ID|Number|Title|Description|Estimate|Epic|Labels"
    }
}