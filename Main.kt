package flashcards

import java.io.File
import kotlin.random.Random

class FlashCards {
    private val cards = mutableMapOf<String, String>()

    init {
        menu()
    }

    private fun <K, V> getKey(map: Map<K, V>, target: V): K {
        return map.keys.first { target == map[it] }
    }

    private fun addTerm(): String {
        val card = readln()
        return if (cards.keys.contains(card)) {
            println("The term \"$card\" already exists. Try again:")
            addTerm()
        } else {
            card
        }
    }

    private fun addDefinition(): String {
        val definition = readln()
        return if (cards.values.contains(definition)) {
            println("The definition \"$definition\" already exists. Try again:")
            addDefinition()
        } else {
            definition
        }
    }
    private fun addFlashCard(index: Int) {
        println("Card #${index + 1}")
        val card = addTerm()
        println("The definition of the card:")
        cards[card] = addDefinition()
        println("The pair (\"$card\":\"${cards[card]}\") has been added.")
    }

    private fun ask() {
        println("How many times to ask?")
        repeat(readln().toInt()) {
            val element = cards.entries.elementAt(Random.nextInt(0, cards.size))

            println("Print the definition of \"${element.key}\":")
            val definition = readln()
            if (definition == element.value) {
                println("Correct!")
            } else if (cards.values.contains(definition)) {
                println("Wrong. The right answer is \"${element.value}\", " +
                        "but your definition is correct for \"${getKey(cards, definition)}\".")
            } else {
                println("Wrong. The right answer is \"${element.value}\".")
            }
        }
    }

    private fun removeCard() {
        println("Which card?")
        val card = readln()
        if (cards.keys.contains(card)) {
            cards.remove(card)
            println("The card has been removed.")
        } else {
            println("Can't remove \"$card\": there is no such card.")
        }
    }

    private fun export() {
        println("File name:")
        val file = File(readln())
        if (!file.exists()) file.createNewFile()
        file.writeText("")
        cards.forEach { (k, v) -> file.appendText("$k-$v\n") }
        println("${cards.size} cards have been saved.")
    }

    private fun import() {
        println("File name:")
        val file = File(readln())
        if (!file.exists()) println("File not found.").also { return }
        val savedCards = file.readLines()
        savedCards.forEach {
            val data = it.split("-")
            cards[data[0]] = data[1]
        }

        println("${savedCards.size} cards have been loaded.")
    }

    private fun menu() {
        while (true) {
            println("Input the action (add, remove, import, export, ask, exit):")
            when(readln()) {
                "add" -> addFlashCard(cards.size)
                "remove" -> removeCard()
                "import" -> import()
                "export" -> export()
                "ask" -> ask()
                "exit" -> break
            }
        }
        println("Bye bye!")
    }
}

fun main() {
    FlashCards()
}
