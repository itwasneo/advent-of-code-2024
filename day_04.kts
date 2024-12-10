import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.time.measureTime

val dim = 140
val input: Array<CharArray?> = arrayOfNulls(dim)
fun readFile() {
    var r = 0
    Files.readAllLines(Path("input\\day04_input.txt")).forEach { line ->
        input[r] = line.trim().toCharArray()
        r++
    }
}

val ord = charArrayOf('X', 'M', 'A', 'S')
val dir = arrayOf(
    Pair(1, 0),
    Pair(0, 1),
    Pair(-1, 0),
    Pair(0, -1),
    Pair(-1, 1),
    Pair(1, 1),
    Pair(1, -1),
    Pair(-1, -1)
)

fun solve1() {
    var result = 0
    for (i in input.indices) {
        for (j in input[0]!!.indices) {
            for (d in dir) {
                var ti = i
                var tj = j
                var valid = true
                for (k in ord) {
                    if (ti < 0 || ti > input.size - 1 || tj < 0 || tj > input.size - 1) {
                        valid = false
                        break
                    }
                    if (input[ti]!![tj] != k) {
                        valid = false
                        break
                    }
                    ti += d.second
                    tj += d.first
                }
                if (valid) {
                    result++
                }
            }
        }
    }
    println("1: $result")
}

fun solve2() {
    var result = 0
    for (i in 1..<input.size - 1) {
        for (j in 1..<input[0]!!.size - 1) {
            if (input[i]!![j] == 'A') {
                val c1 = input[i + 1]!![j - 1]
                val c2 = input[i - 1]!![j + 1]
                val c3 = input[i - 1]!![j - 1]
                val c4 = input[i + 1]!![j + 1]
                if ((c1 == 'M' && c2 == 'S' || c1 == 'S' && c2 == 'M') &&
                    (c3 == 'M' && c4 == 'S' || c3 == 'S' && c4 == 'M')
                ) {
                    result += 1
                }
            }
        }
    }
    println("2: $result")
}

val p = measureTime {
    readFile()
}

val t = measureTime {
    solve1()
    solve2()
}

println("P: $p")
println("T: $t")