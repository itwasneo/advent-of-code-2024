import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs
import kotlin.time.measureTime

val size = 141
val filename = "input\\day16_input.txt"
var start: Vec2? = null
var end: Vec2? = null
val graph = mutableMapOf<Vec2, MutableList<Vec2>>()
val grid = Array(size) { BooleanArray(size) { false } }

val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
val dirs = listOf(right, down, left, up)


fun readInput() {
    Files.readAllLines(Paths.get(filename))
        .forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[y][x] = char == '#'
                if (char == 'S') {
                    start = Vec2(x, y)
                } else if (char == 'E') {
                    end = Vec2(x, y)
                }
            }
        }

    grid.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (!c) {
                dirs.forEach { dir ->
                    val adj = Vec2(x + dir.x, y + dir.y)
                    if (!grid[adj.y][adj.x]) {
                        graph.getOrPut(Vec2(x, y)) { mutableListOf() }
                            .apply { add(adj) }
                    }
                }
            }
        }
    }
}

fun dijkstra(): Map<Vec2, Int> {
    val start = start!!
    val distances = mutableMapOf<Vec2, Int>().withDefault { Int.MAX_VALUE }
    distances[start] = 0

    val priorityQueue =
        PriorityQueue(compareBy<Triple<Vec2, Int, Vec2>> { it.second })
    priorityQueue.add(Triple(start, 0, start))

    while (priorityQueue.isNotEmpty()) {
        val (current, currentDist, previous) = priorityQueue.poll()
        if (current == Vec2(3, 135)) {
            println("hellp")
        }
        if (currentDist > distances.getValue(current)) continue

        graph[current]?.forEach { neighbor ->
            val weight = calculateWeight(current, previous, neighbor)
            if (weight != null) {
                val newDist = currentDist + weight
                if (newDist < distances.getValue(neighbor)) {
                    distances[neighbor] = newDist
                    priorityQueue.add(Triple(neighbor, newDist, current))
                }
            }
        }
    }

    grid.forEachIndexed { y, r ->
        print("$y ->> ".padEnd(10))
        r.forEachIndexed { x, c ->
            if (c) print("#".padEnd(6, '#'))
            else print(distances[Vec2(x, y)].toString().padEnd(6))
        }
        println("")
    }

    print("          ")
    (0..size).forEach { print(it.toString().padEnd(6)) }
    println("1: ${distances[end]}")

    return distances
}

fun calculateWeight(current: Vec2, previous: Vec2, next: Vec2): Int? {
    return if (previous == next) { // Skip backward moves
        null
    } else if (abs((previous - next).x) == 2 || abs((previous - next).y) == 2) {
        1
    } else if (current == previous && next + left == current) { // Only for the start
        1
    } else {
        1001
    }
}


// Util ------------------------------------------------------------------------

/**
 * Rotates given direction vector counterclockwise
 */
data class PathComponent(
    val position: Vec2,
    val direction: Vec2,
    val score: Int
)

fun rotate(direction: Vec2): Vec2 {
    return when (direction) {
        right -> up
        up -> left
        left -> down
        down -> right
        else -> throw RuntimeException("THIS SHOULDN'T HAPPEN")
    }
}

fun clockwiseRotate(direction: Vec2): Vec2 {
    return when (direction) {
        right -> down
        up -> right
        left -> up
        down -> left
        else -> throw RuntimeException("THIS SHOULDN'T HAPPEN")
    }
}

fun oppositeDirection(direction: Vec2): Vec2 {
    return when (direction) {
        right -> left
        up -> down
        left -> right
        down -> up
        else -> throw RuntimeException("This shouldn't happen")
    }
}

val p = measureTime {
    readInput()
}
println("P: $p")

val t1 = measureTime {
    dijkstra()
}
println("T1: $t1")

data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)

    override fun toString(): String {
        return if (x == 1 && y == 0) {
            "RIGHT"
        } else if (x == 0 && y == 1) {
            "DOWN"
        } else if (x == -1 && y == 0) {
            "LEFT"
        } else if (x == 0 && y == -1) {
            "UP"
        } else {
            "($x - $y)"
        }
    }

}
