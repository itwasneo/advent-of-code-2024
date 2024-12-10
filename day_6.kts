import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val size = 130
val roadMap = mutableSetOf<Vec2>() // Original guard path
val obstMap =
    Array(size) { BooleanArray(size) { false } } // 2D array representing the obstruction positions
var startPos: Vec2? = null // Guard's starting position
val startDir = Vec2(0, -1)

/**
 * Like the idea of proper Vec2
 */
data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)
}

/**
 * Just walk the grid
 */
fun solve1() {
    var pos = startPos!!
    var dir = startDir
    while (pos.x in obstMap[0].indices && pos.y in obstMap.indices) {
        if (obstMap[pos.y][pos.x]) {
            pos -= dir
            dir = dir.next()
        }
        roadMap.add(pos)
        pos += dir
    }
    val result = roadMap.size
    println("1: $result")
}


/**
 * Checks whether the upcoming path has a cyclic path by checking the already observed edges
 * It always starts from the Guard's starting position
 */
fun checkCyclic(edges: MutableSet<Pair<Int, Int>>): Boolean {
    var pos = startPos ?: return false
    var dir = startDir
    while (pos.x in obstMap.indices && pos.y in obstMap[0].indices) {
        if (obstMap[pos.y][pos.x]) { // If you are on an obstruction go back and change direction
            pos -= dir
            dir = dir.next()
        }
        val edge = to1D(pos - dir) to to1D(pos)
        if (!edges.add(edge)) return true // If you went over an edge, you already visited CYCLIC
        pos += dir
    }
    return false
}

/**
 * For each vertices in the road map, puts an obstruction to that position and
 * checks the path for cyclic paths.
 */
fun solve2() {
    var result = 0
    val edges =
        mutableSetOf<Pair<Int, Int>>() // Store edges instead of vertices (using a global set and resetting it more performant)
    roadMap.forEach { p ->
        obstMap[p.y][p.x] = true
        if (checkCyclic(edges)) {
            result++
        }
        edges.clear()
        obstMap[p.y][p.x] = false
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
fun Vec2.next(): Vec2 = when (this) {
    Vec2(0, -1) -> Vec2(1, 0)
    Vec2(1, 0) -> Vec2(0, 1)
    Vec2(0, 1) -> Vec2(-1, 0)
    Vec2(-1, 0) -> Vec2(0, -1)
    else -> throw RuntimeException()
}

/**
 * Utility to convert x and y position to single integer for better storage
 */
fun to1D(v: Vec2): Int = v.x * size + v.y

fun readInput() {
    Files.readAllLines(Paths.get("input\\day06_input.txt"))
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
                            Vec2(
                                j,
                                i
                            )
                        ) // Adding Guard's starting position
                        startPos = Vec2(j, i)
                    }
                }
            }
        }
}
