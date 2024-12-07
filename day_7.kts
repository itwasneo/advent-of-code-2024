import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Relatively easy puzzle, just implemented a recursion with accumulators
 */
fun readAndSolve() {
    var part1 = 0L
    var part2 = 0L
    Files.newInputStream(Paths.get("input\\day7_input.txt")).use {
        val br = BufferedReader(InputStreamReader(it))
        var line: String? = br.readLine()
        while (!line.isNullOrEmpty()) {
            val (res, nums) = line.split(":", limit = 2).map(String::trim)
            val target = res.toLong()
            val numbers = nums.split(" ").map(String::trim).map(String::toLong)

            if (addMul(
                    numbers.first(),
                    numbers.drop(1),
                    target
                )
            ) part1 += target // Part1 Calculation
            if (addMulConcat(
                    numbers.first(),
                    numbers.drop(1),
                    target
                )
            ) part2 += target // Part2 Calculation
            line = br.readLine()
        }

    }
    println("1: $part1")
    println("2: $part2")
}

fun addMul(acc: Long, i: List<Long>, t: Long): Boolean {
    if (i.isNotEmpty()) {
        val next = i.first()
        return addMul(acc + next, i.drop(1), t) ||
                addMul(acc * next, i.drop(1), t)
    } else {
        return acc == t
    }
}

fun addMulConcat(acc: Long, i: List<Long>, t: Long): Boolean {
    if (i.isNotEmpty()) {
        val next = i.first()
        return addMulConcat(acc + next, i.drop(1), t) ||
                addMulConcat(acc * next, i.drop(1), t) ||
                addMulConcat("$acc$next".toLong(), i.drop(1), t)
    } else {
        return acc == t
    }
}

val t = measureTime {
    readAndSolve()
}
println("T: $t")
