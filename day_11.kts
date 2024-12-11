import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val fileName = "input\\day11_input.txt"
var input: List<String>? = null
fun readInput() {
    input = Files.readString(Paths.get(fileName)).split(" ")
}

fun solve1() {
    var copiedList = input!!
    repeat(25) {
        copiedList = convert(copiedList)
    }
    println("1: ${copiedList.size}")
}

fun convert(input: List<String>): List<String> {
    val output = mutableListOf<String>()
    input.forEach { str ->
        if (str == "0") {
            output.add("1")
        } else if (str.length % 2 == 0) {
            val m = str.length / 2
            val l = str.substring(0, m)
            val trimmed = str.substring(m).trimStart { it == '0' }
            output.add(l)
            output.add(trimmed.ifEmpty { "0" })
        } else {
            output.add("${str.toLong() * 2024L}")
        }
    }
    return output
}

val p = measureTime {
    readInput()
}
println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")