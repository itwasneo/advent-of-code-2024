import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

val size = 141
val grid = Array(size) { BooleanArray(size) { false } }
var s = Vec2(0, 0)
var e = Vec2(0, 0)
val graph = mutableMapOf<Vec2, MutableSet<Vec2>>()
val possibleCheats = mutableMapOf<Vec2, Set<Vec2>>()

val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
val dirs = listOf(right, down, left, up)

fun readInput() {
    Files.readAllLines(Paths.get("input\\day20_input.txt"))
        .forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[y][x] = char == '#'
                when (char) {
                    'S' -> {
                        s = Vec2(x, y)
                    }

                    'E' -> {
                        e = Vec2(x, y)
                    }

                    else -> {
                        graph[Vec2(x, y)] = mutableSetOf()
                    }
                }
            }
        }

    grid.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (!c) {
                val pos = Vec2(x, y)
                val neighbors = mutableSetOf<Vec2>()
                for (dir in dirs) {
                    val newPos = pos + dir
                    if (newPos.x < 0 || newPos.x >= size || newPos.y < 0 || newPos.y >= size) {
                        continue
                    }
                    if (!grid[newPos.y][newPos.x]) {
                        neighbors.add(newPos)
                    }
                }
                graph[pos] = neighbors
            } else {
                val pc = checkFourDirection(Vec2(x, y))
                if (pc != null) {
                    possibleCheats[pc.first] = pc.second
                }
            }
        }
    }
}

fun checkFourDirection(pos: Vec2): Pair<Vec2, Set<Vec2>>? {
    // Vertical
    val left = pos + left
    val right = pos + right
    if (checkVecInGrid(left) && checkVecInGrid(right) && !grid[left.y][left.x] && !grid[right.y][right.x]) {
        return pos to setOf(left, right)
    }

    // Horizontal
    val up = pos + up
    val down = pos + down
    if (checkVecInGrid(up) && checkVecInGrid(down) && !grid[up.y][up.x] && !grid[down.y][down.x]) {
        return pos to setOf(up, down)
    }
    return null
}

fun checkVecInGrid(pos: Vec2): Boolean {
    return pos.x in 0..<size && pos.y >= 0 && pos.y < size
}

fun shortestPath(start: Vec2, end: Vec2, graph: Map<Vec2, Set<Vec2>>): Int {
    val queue = PriorityQueue<Pair<Vec2, Int>>(compareBy { it.second })
    val visited = mutableSetOf(start)
    val distances = mutableMapOf(start to 0)
    queue.add(start to 0)
    while (queue.isNotEmpty()) {
        val (current, distance) = queue.poll()
        if (current == end) {
            break
        }
        if (visited.contains(current) && distances.getOrDefault(
                current,
                Int.MAX_VALUE
            ) < distance
        ) {
            continue
        }
        for (neighbor in graph[current]!!) {
            if (distances.getOrDefault(
                    neighbor,
                    Int.MAX_VALUE
                ) > distance + 1
            ) {
                distances[neighbor] = distance + 1
                queue.add(neighbor to distance + 1)
            }
            visited.add(neighbor)
        }
    }

    return distances[end]!!
}

fun solve1() {
    val totalDistance = shortestPath(s, e, graph)
    var cnt = 0
    possibleCheats.forEach { cheat ->
        graph[cheat.key] = cheat.value.toMutableSet()
        cheat.value.forEach { graph[it]!!.add(cheat.key) }
        val distance = shortestPath(s, e, graph)
        if (totalDistance - distance >= 100) {
            cnt++
        }
        graph.remove(cheat.key)
        cheat.value.forEach { graph[it]!!.remove(cheat.key) }
    }
    println("1: $cnt")
}

val p = measureTime {
    readInput()
}

println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Int) = Vec2(x * scalar, y * scalar)
    operator fun div(scalar: Int) = Vec2(x / scalar, y / scalar)
    //fun manhattan() = Math.abs(x) + Math.abs(y)
}