import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs
import kotlin.time.measureTime

val size = 17
val filename = "input\\day16_input_example.txt"
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
    val distances1 = dijkstra(start!!, end!!)
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
