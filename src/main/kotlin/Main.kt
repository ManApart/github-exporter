import api.LiveApi
import api.MockApi

fun main(args: Array<String>) {
    val params = Params(args)
    val api = if (params.mock) {MockApi()} else { LiveApi(params)}
    val previousCards = getPreviousCards(params.previousCSVPath)
    val cardPrinter = CardPrinter(api, previousCards)
    val cards = cardPrinter.getCards(params.owner)
    cardPrinter.printCards(cards)
    println("Printed ${cardPrinter.cardsFetched} updated cards and ${cardPrinter.cardsPreserved} non-updated cards.")
}







