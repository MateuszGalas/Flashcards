package flashcards

import java.io.File
import kotlin.random.Random

class FlashCards(import: String, private val export: String) {
    private val cards = mutableMapOf<String, Pair<String, Int>>()
    private var log = ""

    init {
        if (import != "") import(import)
        menu()
    }

    private fun <K, V> getKey(map: Map<K, V>, target: V): K {
        return map.keys.first { target == map[it] }
    }

    // Saving string to log
    private fun String.log(): String {
        log += "$this\n"
        return this
    }
    // Adding cards with definitions to collection
    private fun addFlashCard(index: Int) {
        printAndLog("Card #${index + 1}")
        val card = readln().log()
        if (cards.keys.contains(card)) {
            printAndLog("The card \"$card\" already exists.")
            return
        }
        printAndLog("The definition of the card:")
        val definition = readln().log()
        cards.values.forEach {
            if(it.first == definition) {
                printAndLog("The definition \"$definition\" already exists.")
                return
            }
        }
        cards[card] = Pair(definition, 0)
        printAndLog("The pair (\"$card\":\"${cards[card]?.first}\") has been added.")
    }

    private fun show() {
        printAndLog(cards)
    }
    // Asking for definition of randomly chosen card
    private fun ask() {
        printAndLog("How many times to ask?")
        loop@for(i in 0 until readln().log().toInt()) {
            val element = cards.entries.elementAt(Random.nextInt(0, cards.size))

            printAndLog("Print the definition of \"${element.key}\":")
            val definition = readln().log()
            if (definition == element.value.first) {
                printAndLog("Correct!")
                continue
            }
            // If wrong, checking if definition match to another term
            for (it in cards.values) {
                if(it.first == definition) {
                    printAndLog("Wrong. The right answer is \"${element.value.first}\", " +
                            "but your definition is correct for \"${getKey(cards, Pair(it.first, it.second))}\".")
                    cards[element.key] = Pair(element.value.first, element.value.second + 1)
                    continue@loop
                }
            }
            cards[element.key] = Pair(element.value.first, element.value.second + 1)
            printAndLog("Wrong. The right answer is \"${element.value.first}\".")
        }
    }
    // Removing card from collection
    private fun removeCard() {
        printAndLog("Which card?")
        val card = readln().log()
        if (cards.keys.contains(card)) {
            cards.remove(card)
            printAndLog("The card has been removed.")
        } else {
            printAndLog("Can't remove \"$card\": there is no such card.")
        }
    }
    // Exporting cards to file
    private fun export(fileName: String) {
        val file = if (fileName == "") {
            printAndLog("File name:")
            File(readln().log())
        } else {
            File(fileName)
        }

        if (!file.exists()) file.createNewFile()
        file.writeText("")
        cards.forEach { (k, v) -> file.appendText("$k-${v.first}-${v.second}\n") }
        printAndLog("${cards.size} cards have been saved.")
    }
    // Importing cards from file
    private fun import(fileName: String) {
        val file = if (fileName == "") {
            printAndLog("File name:")
            File(readln().log())
        } else {
            File(fileName)
        }
        if (!file.exists()) printAndLog("File not found.").also { return }
        val savedCards = file.readLines()
        savedCards.forEach {
            val data = it.split("-")
            cards[data[0]] = Pair(data[1], data[2].toInt())
        }

        printAndLog("${savedCards.size} cards have been loaded.")
    }
    // Reset mistakes made by player
    private fun reset() {
        cards.keys.forEach {
            cards[it] = Pair(cards[it]!!.first, 0)
        }
        printAndLog("Card statistics have been reset.")
    }
    // Check and print card with the most mistakes made
    private fun hardestCard() {
        val list = mutableMapOf<String, Int>()
        cards.keys.forEach {
            if (cards[it]!!.second > 0) list[it] = cards[it]!!.second
        }
        if (list.isEmpty()) printAndLog("There are no cards with errors.").also { return }

        var mistakes = list.toList().sortedBy { (_, value) -> value }.toMap()
        mistakes = mistakes.filter { it.value ==  mistakes.values.last()}
        printAndLog("The hardest card${if (mistakes.size > 1) "s" else ""} " +
                "${if (mistakes.size > 1) "are" else "is"} " +
                "\"${mistakes.keys.joinToString("\", \"")}\". " +
                "You have ${mistakes.values.first()} errors answering ${if (mistakes.size > 1) "them" else "it"}.")
    }
    // Save log to file
    private fun log() {
        printAndLog("File name:")
        val file = File(readln().log())
        if (!file.exists()) file.createNewFile()
        printAndLog("The log has been saved")
        file.writeText(log)
    }
    // Print and save text to log
    private fun printAndLog(text: Any) {
        println(text)
        log += "$text\n"
    }
    // Menu
    private fun menu() {
        while (true) {
            printAndLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            when(readln().log()) {
                "add" -> addFlashCard(cards.size)
                "remove" -> removeCard()
                "import" -> import("")
                "export" -> export("")
                "ask" -> ask()
                "log" -> log()
                "hardest card" -> hardestCard()
                "reset stats" -> reset()
                "show" -> show()
                "exit" -> {
                    if (export != "") {
                        export(export)
                        break
                    } else {
                        break
                    }
                }
            }
        }
        printAndLog("Bye bye!")
    }
}

fun main(args: Array<String>) {
    val import = args.indexOf("-import")
    val export = args.indexOf("-export")
    var importFile = ""
    var exportFile = ""

    if (import != -1) importFile = args[import + 1]
    if (export != -1) exportFile = args[export + 1]

    FlashCards(importFile, exportFile)
}
