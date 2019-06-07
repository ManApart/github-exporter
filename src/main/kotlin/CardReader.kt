import java.io.BufferedReader
import java.io.FileReader

fun getPreviousCards(csvPath: String): Map<String, Map<Int, Card>> {
    return if (csvPath == "none") {
        mapOf()
    } else {
        readCards(csvPath)
    }
}

private fun readCards(csvPath: String): Map<String, Map<Int, Card>> {
    val fileReader = BufferedReader(FileReader(csvPath))
    val cards = mutableMapOf<String, MutableMap<Int, Card>>()

    // Read CSV header
    fileReader.readLine()

    // Read the file line by line starting from the second line
    var line = fileReader.readLine()
    while (line != null) {
        val tokens = line.split("|")
        if (tokens.isNotEmpty()) {
            val card = Card.fromCSV(tokens)
            cards.putIfAbsent(card.repoName, mutableMapOf())
            cards[card.repoName]!![card.number] = card
        }
        line = fileReader.readLine()
    }
    return cards
}