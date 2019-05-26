
import api.MockApi
import org.junit.Test

class MockRunner {

    @Test
    fun mockedMain() {
        val api = MockApi()
        val cardPrinter = CardPrinter(api)
        val cards = cardPrinter.getCards("owner")
        cardPrinter.printCards(cards)
        println("Done")
    }
}