import api.LiveApi
import api.MockApi

//Eventually replace required repo id with all repos for an owner

fun main(args: Array<String>) {
    val params = Params(args)
    val api = if (params.mock) {MockApi()} else { LiveApi(params)}
    val cardPrinter = CardPrinter(api)
    val cards = cardPrinter.getCards()
    cardPrinter.printCards(cards)
    println("Done")
}





