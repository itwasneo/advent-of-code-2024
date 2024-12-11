import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Not much to explain, another easy puzzle just prune the repeated values at
 * each cycle so that it doesn't use so much memory.
 */
val fileName = "input\\day11_input.txt"
var input: List<Pair<String, Long>> = mutableListOf()
fun readInput() {
    input = Files.readString(Paths.get(fileName)).split(" ")
        .groupingBy { it }
        .eachCount()
        .map { (key, value) -> key to value.toLong() }
}

fun solve1() {
    var copied = input
    repeat(25) {
        copied = convert(copied)
    }
    val result = copied.sumOf { it.second }
    println("1: $result")
}

fun solve2() {
    repeat(75) {
        input = convert(input)
    }
    val result = input.sumOf { it.second }
    println("2: $result")
}

fun convert(input: List<Pair<String, Long>>): List<Pair<String, Long>> {
    val newList = mutableListOf<Pair<String, Long>>()
    input.forEach { (num, amount) ->
        if (num == "0") {
            newList.add(Pair("1", amount))
        } else if ((num.length and 1) != 0) {
            val newVal = "${num.toLong() * 2024}"
            newList.add(Pair(newVal, amount))
        } else {
            val m = num.length / 2
            val l = num.substring(0, m)
            val r = num.substring(m).trimStart { it == '0' }.ifEmpty { "0" }
            newList.add(Pair(l, amount))
            newList.add(Pair(r, amount))
        }
    }

    return newList.groupBy { it.first }
        .map { (key, value) -> key to value.sumOf { it.second } }
}

val p = measureTime {
    readInput()
}
println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")

val t2 = measureTime {
    solve2()
}
println("T1: $t2")
