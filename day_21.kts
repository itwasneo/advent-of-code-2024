import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

var numberMovementMap = mutableMapOf<Char, MutableMap<Char, List<String>>>()
var directionMovementMap = mutableMapOf<Char, MutableMap<Char, List<String>>>()

data class Position(val x: Int, val y: Int)

val keypad = mapOf(
    '7' to Position(0, 0), '8' to Position(1, 0), '9' to Position(2, 0),
    '4' to Position(0, 1), '5' to Position(1, 1), '6' to Position(2, 1),
    '1' to Position(0, 2), '2' to Position(1, 2), '3' to Position(2, 2),
    '0' to Position(1, 3), 'A' to Position(2, 3)
)

val directions = listOf(
    Pair(0, -1) to '^', // Up
    Pair(0, 1) to 'v',  // Down
    Pair(-1, 0) to '<', // Left
    Pair(1, 0) to '>'   // Right
)

val directionKeypad = mapOf(
    '^' to Position(1, 0), 'A' to Position(2, 0),
    '<' to Position(0, 1), 'v' to Position(1, 1), '>' to Position(2, 1)
)

fun bfsAllPaths(
    start: Position,
    end: Position,
    kp: Map<Char, Position>
): List<String> {
    val queue: Queue<Pair<Position, String>> = LinkedList()
    val visited = mutableMapOf<Position, Int>()
    val paths = mutableListOf<String>()
    queue.add(start to "")
    visited[start] = 0

    while (queue.isNotEmpty()) {
        val (current, path) = queue.poll()
        if (current == end) {
            paths.add(path)
            continue
        }

        for ((dir, move) in directions) {
            val next = Position(current.x + dir.first, current.y + dir.second)
            val newPath = path + move
            if (next in kp.values) {
                val newDist = newPath.length
                if (next !in visited || newDist <= visited[next]!!) {
                    queue.add(next to newPath)
                    visited[next] = newDist
                }
            }
        }
    }
    return paths
}

fun buildAllMovementMap() {
    for ((startKey, startPos) in keypad) {
        numberMovementMap[startKey] = mutableMapOf()
        for ((endKey, endPos) in keypad) {
            if (startKey != endKey) {
                numberMovementMap[startKey]!![endKey] =
                    bfsAllPaths(startPos, endPos, keypad)
            }
        }
    }
}

fun buildDirectionMovementMap() {
    for ((startKey, startPos) in directionKeypad) {
        directionMovementMap[startKey] = mutableMapOf()
        for ((endKey, endPos) in directionKeypad) {
            if (startKey != endKey) {
                directionMovementMap[startKey]!![endKey] =
                    bfsAllPaths(startPos, endPos, directionKeypad)
            }
        }
    }
}

fun chainRobots(numberSequence: String): String {
    var currentNumber = 'A'
    var result = StringBuilder()
    numberSequence.forEach { number ->
        val numberPath = getNumberKeypadMovements(currentNumber, number)

        var currentDirection = 'A'
        numberPath.forEach { direction ->
            val directionPath =
                getDirectionKeypadMovements(currentDirection, direction)
            val last = directionPath.last()
            val pressA = getDirectionKeypadMovements(last, 'A')
            result.append(directionPath + 'A' + pressA)
            currentDirection = last
        }
        result.append('A')
        currentNumber = number
    }
    return result.toString()
}

fun getDirectionKeypadMovements(start: Char, end: Char): String {
    val movements =
        directionMovementMap[start]?.get(end)?.firstOrNull() ?: return ""
    return movements
}

fun getNumberKeypadMovements(start: Char, end: Char): String {
    val movements =
        numberMovementMap[start]?.get(end)?.firstOrNull() ?: return ""
    return movements
}

fun solve1() {
    buildAllMovementMap()
    buildDirectionMovementMap()
    /*
    numberMovementMap.forEach { (start, destinations) ->
        println("From $start:")
        destinations.forEach { (end, paths) ->
            println("  To $end: ${paths.joinToString(", ")}")
        }
    }
    directionMovementMap.forEach { (start, destinations) ->
        println("From $start:")
        destinations.forEach { (end, paths) ->
            println("  To $end: ${paths.joinToString(", ")}")
        }
    }
     */
    println(chainRobots("029A"))
}

fun readInput() {
    Files.readAllLines(Paths.get("input\\day21_input_example.txt"))
        .forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                print(char)
            }
            println()
        }
}

/*
val p = measureTime {
    readInput()
}

println(p)

 */

val t1 = measureTime {
    solve1()
}

println(t1)