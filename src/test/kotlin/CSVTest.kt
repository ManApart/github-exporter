import api.MockApi
import org.junit.Assert.assertEquals
import org.junit.Test

class CSVTest {

    @Test
    fun outputMatches() {
        val api = MockApi()
        val cardPrinter = CardPrinter(api)
        val cards = cardPrinter.getCards("owner")

        val expected = """
        10|repoA|10|This is a title for 10|This is a description for 10|1|This is a title for 1|milestone|pipeline||
        11|repoA|11|This is a title for 11|This is a description for 11|2|This is a title for 1|milestone|pipeline||
        12|repoA|12|This is a title for 12|This is a description for 12|3|This is a title for 1|milestone|pipeline||
        21|repoA|21|This is a title for 21|This is a description for 21|1|This is a title for 2|milestone|pipeline||
        22|repoA|22|This is a title for 22|This is a description for 22|2|This is a title for 2|milestone|pipeline||
        23|repoA|23|This is a title for 23|This is a description for 23|3|This is a title for 2|milestone|pipeline||
        31|repoA|31|This is a title for 31|This is a description for 31|1|This is a title for 3|milestone|pipeline||
        32|repoA|32|This is a title for 32|This is a description for 32|2|This is a title for 3|milestone|pipeline||
        33|repoA|33|This is a title for 33|This is a description for 33|3|This is a title for 3|milestone|pipeline||
        40|repoB|40|This is a title for 40|This is a description for 40|1|This is a title for 4|milestone|pipeline||
        41|repoB|41|This is a title for 41|This is a description for 41|2|This is a title for 4|milestone|pipeline||
        42|repoB|42|This is a title for 42|This is a description for 42|3|This is a title for 4|milestone|pipeline||
        51|repoB|51|This is a title for 51|This is a description for 51|1|This is a title for 5|milestone|pipeline||
        52|repoB|52|This is a title for 52|This is a description for 52|2|This is a title for 5|milestone|pipeline||
        53|repoB|53|This is a title for 53|This is a description for 53|3|This is a title for 5|milestone|pipeline||
        61|repoB|61|This is a title for 61|This is a description for 61|1|This is a title for 6|milestone|pipeline||
        62|repoB|62|This is a title for 62|This is a description for 62|2|This is a title for 6|milestone|pipeline||
        63|repoB|63|This is a title for 63|This is a description for 63|3|This is a title for 6|milestone|pipeline||
        """.trimIndent()

        val actual = cards.joinToString("\n") {it.print()}

        assertEquals(expected, actual)

    }
}