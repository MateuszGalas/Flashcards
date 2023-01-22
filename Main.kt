package flashcards

fun main() {
    val cards = mutableMapOf<String, String>()
    println("Input the number of cards:")

    repeat(readln().toInt()) {
        println("Card #${it + 1}")
        val card = readln()
        println("The definition for card #${it + 1}")
        cards[card] = readln()
    }

    cards.keys.forEach {
        println("Print the definition of \"$it\":")
        if (readln() == cards[it]) println("Correct!") else println("Wrong. The right answer is \"${cards[it]}\".")
    }
}
