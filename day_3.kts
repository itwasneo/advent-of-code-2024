import java.io.File
import kotlin.time.measureTime

val content = File("C:\\Users\\kaany\\Desktop\\workspace\\advent-of-code-2024\\input\\day3_input.txt").readText()
fun solve1() {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val part1 = regex.findAll(content).map { matchResult ->
        matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
    }.sum()

    println("Part1: $part1")
}
private val DO = """do\(\)"""
private val DONT = """don't\(\)"""
private val MUL = """mul\((\d{1,3}),(\d{1,3})\)"""

fun solve2() {
    val doIndices = mutableListOf<Int>()
    val dontIndices = mutableListOf<Int>()
    val muls = mutableListOf<Triple<Int, Int, Int>>()

    val patterns = listOf(DO, DONT, MUL)
    val newContent = "do()" + content + "don't()"

    patterns.forEach { p ->
        val matches = Regex(p).findAll(newContent)
        matches.forEach { m ->
            when(p) {
                DO -> doIndices.add(m.range.first)
                DONT -> dontIndices.add(m.range.first)
                MUL -> muls.add(Triple(m.range.first, m.groupValues[1].toInt(), m.groupValues[2].toInt()))
            }
        }
    }

    var s = 0
    var f = 0
    val ranges = mutableListOf<Pair<Int, Int>>()
    var i = 0
    do {
        if (doIndices[i] > dontIndices[f]) {
            ranges.add(Pair(doIndices[s], dontIndices[f]))
            s = i
            f++
            i--
        } else if (i == doIndices.size - 1 && doIndices[i] < dontIndices[f]) {
            ranges.add(Pair(doIndices[s], dontIndices[f]))
        }
        i++
    } while (i < doIndices.size)
    println(doIndices)
    println(dontIndices)
    println(ranges)

    var rangeIndex = 0
    var result = 0
    muls.forEach { m ->
        while (rangeIndex < ranges.size && m.first > ranges[rangeIndex].second) {
            rangeIndex++
        }
        if (rangeIndex < ranges.size && m.first in ranges[rangeIndex].first..ranges[rangeIndex].second) {
            result += m.second * m.third
        }
    }
    println(result)
}


//solve1()
val duration = measureTime {
    solve2()
}
println("2: $duration")
