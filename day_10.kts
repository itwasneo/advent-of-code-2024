import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Another easy one that needs no explanation. This year's problems waay easier
 * than the last year's
 */
val size = 47
var g: Array<IntArray> = Array(size) { IntArray(size) { -1 } }
val dirs = arrayOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))

fun solve1() {
    var result = 0
    g.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell == 0) {
                walk(Pair(x, y), 0)?.let {
                    result += it.size
                }
            }
        }
    }

    println("1: $result")
}

fun walk(currentPosition: Pair<Int, Int>, target: Int): Set<Pair<Int, Int>>? {
    if (!(currentPosition.first in 0..<size && currentPosition.second in 0..<size)) {
        return null
    }
    if (g[currentPosition.second][currentPosition.first] != target) {
        return null
    }
    return if (target == 9) {
        setOf(currentPosition)
    } else {
        val uniqueEnds = mutableSetOf<Pair<Int, Int>>()
        dirs.forEach { dir ->
            val nextPosition = Pair(
                currentPosition.first + dir.first,
                currentPosition.second + dir.second
            )
            walk(nextPosition, target + 1)?.let { uniqueEnds.addAll(it) }
        }
        return uniqueEnds
    }
}

fun walkWithCounts(
    currentPosition: Pair<Int, Int>,
    target: Int
): Int {
    if (!(currentPosition.first in 0..<size && currentPosition.second in 0..<size)) {
        return 0
    }
    if (g[currentPosition.second][currentPosition.first] != target) {
        return 0
    }
    return if (target == 9) {
        1
    } else {
        dirs.sumOf { dir ->
            val nextPosition = Pair(
                currentPosition.first + dir.first,
                currentPosition.second + dir.second
            )
            walkWithCounts(nextPosition, target + 1)
        }
    }
}

fun solve2() {
    var result = 0
    g.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell == 0) {
                result += walkWithCounts(Pair(x, y), 0)
            }
        }
    }
    println("2: $result")
}

fun readInput() {
    Files.readAllLines(Paths.get("input\\day10_input.txt"))
        .forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    g[y][x] = c.digitToInt()
                } else {
                    g[y][x] = -1
                }
            }
        }
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

