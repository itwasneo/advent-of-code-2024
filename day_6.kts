import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val size = 130
val roadMap = mutableSetOf<Pair<Int, Int>>() // Original guard path
val obstMap =
    Array(size) { BooleanArray(size) { false } } // 2D array representing the obstruction positions
var startPos: Pair<Int, Int>? = null // Guard's starting position
typealias DIR = Pair<Int, Int> // Just more readable IMO
val startDir = DIR(0, -1)

/**
 * Just walk the grid
 */
fun solve1() {
    var (x, y) = startPos!!
    var dir = startDir
    while (x in obstMap[0].indices && y in obstMap.indices) {
        if (obstMap[y][x]) {
            x -= dir.first
            y -= dir.second
            dir = dir.next()
            x += dir.first
            y += dir.second
        }
        roadMap.add(Pair(x, y))
        x += dir.first
        y += dir.second
    }
    val result = roadMap.size
    println("1: $result")
}

val edges =
    mutableSetOf<Pair<Int, Int>>() // Store edges instead of vertices (using a global set and resetting it more performant)

/**
 * Checks whether the upcoming path has a cyclic path by checking the already observed edges
 * It always starts from the Guard's starting position
 */
fun checkCyclic(): Boolean {
    var (x, y) = startPos ?: return false
    var dir = startDir
    while (x in obstMap.indices && y in obstMap[0].indices) {
        if (obstMap[y][x]) { // If you are on an obstruction go back and change direction
            x -= dir.first
            y -= dir.second
            dir = dir.next()
        }
        val from = to1D(x - dir.first, y - dir.second)
        val to = to1D(x, y)
        val edge = from to to
        if (!edges.add(edge)) return true // If you went over an edge, you already visited CYCLIC
        x += dir.first
        y += dir.second
    }
    return false
}

/**
 * For each vertices in the road map, puts an obstruction to that position and
 * checks the path for cyclic paths.
 */
fun solve2() {
    var result = 0
    roadMap.forEach { p ->
        obstMap[p.second][p.first] = true
        if (checkCyclic()) {
            result++
        }
        edges.clear()
        obstMap[p.second][p.first] = false
    }
    println("2: $result")
}

val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

val t2 = measureTime {
    solve2()
}

println("P: $p")
println("T1: $t1")
println("T2: $t2")

/**
 * Utility function to get direction, when see obstruction
 */
fun DIR.next(): DIR = when (this) {
    Pair(0, -1) -> Pair(1, 0)
    Pair(1, 0) -> Pair(0, 1)
    Pair(0, 1) -> Pair(-1, 0)
    Pair(-1, 0) -> Pair(0, -1)
    else -> throw RuntimeException()
}

/**
 * Utility to convert x and y position to single integer for better storage
 */
fun to1D(x: Int, y: Int): Int = x * size + y

fun readInput() {
    Files.readAllLines(Paths.get("input\\day6_input.txt"))
        .forEachIndexed { i, l ->
            l.trim().forEachIndexed { j, c ->
                when (c) {
                    '.' -> {
                        // SKIP
                    }

                    '#' -> {
                        obstMap[i][j] = true
                    }

                    else -> {
                        roadMap.add(
                            Pair(
                                j,
                                i
                            )
                        ) // Adding Guard's starting position
                        startPos = Pair(j, i)
                    }
                }
            }
        }
}
