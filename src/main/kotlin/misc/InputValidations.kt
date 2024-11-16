package org.example.misc

fun <T> validate(prompt: String, predicate: (String) -> Boolean, output: (String?) -> T): T {
    val input = readInput(prompt, predicate)
    return output(input)
}

fun readInput(prompt: String, predicate: (String) -> Boolean): String? {
    var message = prompt
    while (true) {
        print("$prompt: ")
        val input = readln()
        try {
            if (predicate(input)) return if (!input.isBlank()) input else null
        } catch (ex: Exception) { message = "${ex.message}, try again" }
        message = "invalid input!, try again"
        println()
    }
}