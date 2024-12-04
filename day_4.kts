import java.io.File
import kotlin.time.measureTime

val dim = 140
val input: Array<CharArray?> = arrayOfNulls(dim)
fun readFile() {
    var r = 0
    File("input\\day4_input.txt").forEachLine { line ->
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

val corners =
    listOf(Pair(Pair(1, -1), Pair(-1, 1)), Pair(Pair(-1, -1), Pair(1, 1)))

fun solve2() {
    var result = 0
    for (i in input.indices) {
        for (j in input[0]!!.indices) {
            if (i == 0 || i == input.size - 1 || j == 0 || j == input.size - 1) {
                // DO NOTHING
            } else {
                if (input[i]!![j] == 'A') {
                    val c1 = input[i + corners[0].first.second]!![corners[0].first.first + j]
                    val c2 = input[i + corners[0].second.second]!![corners[0].second.first + j]
                    val c3 = input[i + corners[1].first.second]!![corners[1].first.first + j]
                    val c4 = input[i + corners[1].second.second]!![corners[1].second.first + j]
                    if (((c1 == 'M' && c2 == 'S') || (c1 == 'S' && c2 == 'M')) &&
                        ((c3 == 'M' && c4 == 'S') || (c3 == 'S' && c4 == 'M'))
                    ) {
                        result += 1
                    }
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