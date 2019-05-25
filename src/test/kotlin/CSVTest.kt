import api.MockApi
import org.junit.Assert.assertEquals
import org.junit.Test

class CSVTest {

    @Test
    fun doThing() {
        val api = MockApi()
        val cardPrinter = CardPrinter(api)
        val cards = cardPrinter.getCards()

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
        """.trimIndent()

        val actual = cards.joinToString("\n") {it.print()}

        assertEquals(expected, actual)

    }
}