import api.MockApi
import org.junit.Assert.assertEquals
import org.junit.Test

class CSVTest {

    @Test
    fun outputMatches() {
        val api = MockApi()
        val cardPrinter = CardPrinter(api)
        val cards = cardPrinter.getCards("owner")

        assertEquals(24, cards.size)

    }
}