class BadFormatException(message: String) : Exception(message)

fun compress(input: String): String {
    if (input.isEmpty()) return ""
    if (input.contains("±")) throw BadFormatException("The input cannot contain the symbol ±")
    val compressed: StringBuilder = StringBuilder()
    var currentChar: Char = input[0]
    var count = 1

    for (i in 1..<input.length + 1) {
        if (i != input.length && input[i] == currentChar) {
            count++
        } else {
            compressed.append("$currentChar±$count")
            if (i == input.length) {
                continue
            }
            currentChar = input[i]
            count = 1
        }
    }
    return compressed.toString()
}

fun checkForValidity(repeatNumber: String) {
    if (repeatNumber.toIntOrNull() == null) {
        throw BadFormatException("The part \"$repeatNumber\" must be an integer")
    }
    if (repeatNumber.toInt() < 1) {
        throw BadFormatException("The repetition number must be positive, not $repeatNumber")
    }
}
fun decompress(compressed: String): String {
    if (compressed.isEmpty()) return ""

    val decompressed: StringBuilder = StringBuilder()
    val parts = compressed.split("±")
    if (parts[0].length != 1) {
        throw BadFormatException("The string must start with one character and a sign \'±\' after it, not with " + parts[0])
    }
    var currentChar = parts[0]
    for (i in 1..<parts.size-1) {
        if (parts[i].isEmpty()) {
            throw BadFormatException("There must be something between \'±\'")
        }
        val repeatNumber = parts[i].subSequence(0, parts[i].length - 1).toString()
        checkForValidity(repeatNumber)
        decompressed.append(currentChar.repeat(repeatNumber.toInt()))
        currentChar = parts[i].last().toString()
    }
    val repeatNumber = parts.last().toString()
    checkForValidity(repeatNumber)
    decompressed.append(currentChar.repeat(repeatNumber.toInt()))
    return decompressed.toString()
}

fun main() {
    println("To compress a string: compress <inputString>\nTo decompress a string: decompress <inputString>")
    var splitInput = readln().split(' ')
    while ((splitInput.size != 2) || (splitInput[0] != "compress" && splitInput[0] != "decompress")) {
        println("The input should consist of two strings, the first string should be \"compress\" or \"decompress\"")
        splitInput = readln().split(' ')
    }
    try {
        when(splitInput[0]) {
            "compress" -> println("Compressed: " + compress(splitInput[1]))
            "decompress" -> println("Decompressed: " + decompress(splitInput[1]))
        }
    } catch (e: Exception) {
        println(e.message)
    }
}
