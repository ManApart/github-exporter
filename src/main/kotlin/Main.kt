import api.LiveApi
import api.MockApi

fun main(args: Array<String>) {
    val params = Params(args)
    val api = if (params.mock) {MockApi()} else { LiveApi(params)}
    val cardPrinter = CardPrinter(api)
    val cards = cardPrinter.getCards(params.owner)
    cardPrinter.printCards(cards)
    if (cards.isNotEmpty()) {
        println("Printed ${cards.size} cards to ${cardPrinter.fileName}.")
    }
}






