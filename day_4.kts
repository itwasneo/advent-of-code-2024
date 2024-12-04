import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.time.measureTime

val dim = 140
val input: Array<CharArray?> = arrayOfNulls(dim)
fun readFile() {
    var r = 0
    Files.readAllLines(Path("input\\day4_input.txt")).forEach { line ->
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

val c = intArrayOf(1, -1, -1, 1, -1, -1, 1, 1)

fun solve2() {
    var result = 0
    for (i in input.indices) {
        for (j in input[0]!!.indices) {
            if (i == 0 || i == input.size - 1 || j == 0 || j == input.size - 1) {
                // DO NOTHING
            } else {
                if (input[i]!![j] == 'A') {
                    val c1 = input[i + c[0]]!![j + c[1]]
                    val c2 = input[i + c[2]]!![j + c[3]]
                    val c3 = input[i + c[4]]!![j + c[5]]
                    val c4 = input[i + c[6]]!![j + c[7]]
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