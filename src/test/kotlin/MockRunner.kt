import api.MockApi
import org.junit.Test

class MockRunner {

    @Test
    fun mockedMain() {
        val api = MockApi()
        val processor = CardProcessor(api)
        val cards = processor.getCards("owner")
        printCards(cards)
        println("Done")
    }
}