import api.LiveApi
import api.MockApi

fun main(args: Array<String>) {
    val params = Params(args)
    val api = if (params.mock) {
        MockApi()
    } else {
        LiveApi(params)
    }
    val processor = CardProcessor(api)
    val cards = processor.getCards(params.owners)
    printCards(cards)
}






