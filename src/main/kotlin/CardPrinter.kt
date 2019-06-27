import java.io.File

fun printCards(cards: List<Card>) {
    if (cards.isNotEmpty()) {
        val fileName = "card_results_${System.currentTimeMillis()}.csv"
        File(fileName).printWriter().use { out ->
            out.println(cards.first().printHeaderRow())
            out.println(cards.joinToString("\n") { it.print() })
        }
//        println(cards.joinToString("\n") {it.print()})
        println("Printed ${cards.size} cards to $fileName.")
    } else {
        println("No cards to print")
    }
}

fun cleanNewLines(input: String): String {
    return input
            .replace("\n", "\\n")
            .replace("\r", "\\n")
}

fun formatDate(input: String): String {
    return input
            .replace("T", " ")
            .replace("Z", "")
}