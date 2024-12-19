import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Very cool algorithm to implement. It's a classic dynamic programming problem.
 */
var patterns: Set<String>? = null
var designs: Set<String>? = null

fun readInput() {
    val input = Files.readString(Paths.get("input\\day19_input.txt"))
    val (p1, p2) = input.split("\r\n\r\n")

    patterns = p1.split(",").map(String::trim).toSet()
    designs = p2.split("\r\n").map(String::trim).toSet()

}

fun solve1() {
    val possible = designs!!.filter { canFormString(it) }

    println("1: ${possible.size}")
}

fun solve2() {
    val result = designs!!.sumOf { canFormStringNTimes(it) }

    println("2: $result")
}

fun canFormString(target: String): Boolean {
    val maxLength = patterns!!.maxOf { it.length }
    val dp = BooleanArray(target.length + 1)
    dp[0] = true

    for (i in 1..target.length) {
        for (j in 1..maxLength) {
            if (i >= j) {
                val word = target.substring(i - j, i)
                if (patterns!!.contains(word)) {
                    dp[i] = dp[i] || dp[i - j]
                }
            }
        }
    }

    return dp[target.length]
}

fun canFormStringNTimes(target: String): Long {
    val maxLength = patterns!!.maxOf { it.length }
    val dp = LongArray(target.length + 1)
    dp[0] = 1

    for (i in 1..target.length) {
        for (j in 1..maxLength) {
            if (i >= j) {
                val word = target.substring(i - j, i)
                if (patterns!!.contains(word)) {
                    dp[i] += dp[i - j]
                }
            }
        }
    }

    return dp[target.length]
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
println("T2: $t2")