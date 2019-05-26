import api.MockApi
import org.junit.Assert.assertEquals
import org.junit.Test

class CSVTest {

    @Test
    fun doThing() {
        val api = MockApi()
        val cardPrinter = CardPrinter(api)
        val cards = cardPrinter.getCards("owner")

        val expected = """
        10|10|This is a title for 10|This is a description for 10|1|This is a title for 1
        11|11|This is a title for 11|This is a description for 11|2|This is a title for 1
        12|12|This is a title for 12|This is a description for 12|3|This is a title for 1
        21|21|This is a title for 21|This is a description for 21|1|This is a title for 2
        22|22|This is a title for 22|This is a description for 22|2|This is a title for 2
        23|23|This is a title for 23|This is a description for 23|3|This is a title for 2
        31|31|This is a title for 31|This is a description for 31|1|This is a title for 3
        32|32|This is a title for 32|This is a description for 32|2|This is a title for 3
        33|33|This is a title for 33|This is a description for 33|3|This is a title for 3
        40|40|This is a title for 40|This is a description for 40|1|This is a title for 4
        41|41|This is a title for 41|This is a description for 41|2|This is a title for 4
        42|42|This is a title for 42|This is a description for 42|3|This is a title for 4
        51|51|This is a title for 51|This is a description for 51|1|This is a title for 5
        52|52|This is a title for 52|This is a description for 52|2|This is a title for 5
        53|53|This is a title for 53|This is a description for 53|3|This is a title for 5
        61|61|This is a title for 61|This is a description for 61|1|This is a title for 6
        62|62|This is a title for 62|This is a description for 62|2|This is a title for 6
        63|63|This is a title for 63|This is a description for 63|3|This is a title for 6
        """.trimIndent()

        val actual = cards.joinToString("\n") {it.print()}

        assertEquals(expected, actual)

    }
}