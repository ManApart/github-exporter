import api.LiveApi

//Eventually replace required repo id with all repos for an owner

fun main(args: Array<String>) {
    val params = Params(args)
    val api = LiveApi(params)
    val cardPrinter = CardPrinter(api)
    val cards = cardPrinter.getCards()
    cardPrinter.printCards(cards)
}





