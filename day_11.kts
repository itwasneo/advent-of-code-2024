import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Not much to explain, another easy puzzle just prune the repeated values at
 * each cycle so that it doesn't use so much memory.
 */
val fileName = "input\\day11_input.txt"
var input = mapOf<String, Long>()
fun readInput() {
    input = Files.readString(Paths.get(fileName)).split(" ")
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, count) -> count.toLong() }
}

fun solve1() {
    var copied = input
    repeat(25) {
        copied = convert(copied)
        //println(copied)
    }
    val result = copied.values.sum()
    println("1: $result")
}

fun solve2() {
    repeat(75) {
        input = convert(input)
    }
    val result = input.values.sum()
    println("2: $result")
}

fun convert(input: Map<String, Long>): Map<String, Long> {
    val newMap = mutableMapOf<String, Long>()
    input.forEach { (num, amount) ->
        if (num == "0") {
            newMap["1"] = newMap.getOrPut("1") { 0 } + amount
        } else if ((num.length and 1) != 0) {
            val newVal = "${num.toLong() * 2024}"
            newMap[newVal] = newMap.getOrPut(newVal) { 0 } + amount
        } else {
            val m = num.length / 2
            val l = num.substring(0, m)
            val r = num.substring(m).trimStart { it == '0' }.ifEmpty { "0" }
            newMap[l] = newMap.getOrPut(l) { 0 } + amount
            newMap[r] = newMap.getOrPut(r) { 0 } + amount
        }
    }

    return newMap
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
