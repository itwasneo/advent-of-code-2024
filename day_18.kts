import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

val size = 71
val grid = Array(size) { BooleanArray(size) { false } }
var start = Vec2(0, 0)
var exit = Vec2(size - 1, size - 1)
var nodes = mutableListOf<Vec2>()

// Direction utilities
val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
val dirs = listOf(right, down, left, up)


// Type represents a single node on a shortest path graph (Direction matters)
//typealias Node = Pair<Vec2, Vec2>

//data class Node(val xy: Vec2, val d: Vec2)

// Start is top left = 0, 0
// Exit is bottom right = size - 1, size - 1

fun readInput(firstN: Int) {
    Files.readAllLines(Paths.get("input\\day18_input.txt")).take(firstN)
        .forEach { xy ->
            val v = xy.split(",", limit = 2).map(String::toInt)
            grid[v[1]][v[0]] = true
        }

    grid.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (c) {
                //print("#")
            } else {
                nodes.add(Vec2(x, y))
                //print(".")
            }
        }
        //print("\n")
    }
}

fun solve1(firstN: Int) {
    readInput(firstN)
    val distances = shortestPath()
    println(distances[exit])
}

fun shortestPath(): Map<Vec2, Int> {
    val distances = mutableMapOf<Vec2, Int>()

    // Initializing each distance with MAX int value
    nodes.forEach { n ->
        distances[n] = Int.MAX_VALUE
    }

    val queue = PriorityQueue(compareBy<Pair<Vec2, Int>> { it.second })
    queue.add(Pair(start, 0))

    while (queue.isNotEmpty()) {
        val (cn, cd) = queue.poll()

        if (cd > distances[cn]!!) continue

        val adjs = getAdjNodes(cn)
        adjs.forEach { neighbor ->
            val newDistance = cd + 1
            if (newDistance < distances[neighbor]!!) {
                distances[neighbor] = newDistance
                queue.add(neighbor to newDistance)
            }
        }
    }

    return distances
}

fun getAdjNodes(node: Vec2): List<Vec2> {
    return dirs.filter { d ->
        val x = node.x + d.x
        val y = node.y + d.y
        x in 0 until size && y in 0 until size && !grid[y][x]
    }.map { node + it }
}

fun solve2(firstN: Int) {
    val reduced =
        Files.readAllLines(Paths.get("input\\day18_input.txt")).drop(firstN)
    var idx = 0
    for (xy in reduced) {
        val v = xy.split(",", limit = 2).map(String::toInt)
        grid[v[1]][v[0]] = true
        nodes.remove(Vec2(v[0], v[1]))
        val distances = shortestPath()
        if (distances[exit] == Int.MAX_VALUE) {
            println("2: ${reduced[idx]}")
            break
        }
        idx++
    }
}

/*
val p = measureTime {
    readInput()
}
println("P: $p")
 */

val t1 = measureTime {
    solve1(1024)
}
println("T1: $t1")

val t2 = measureTime {
    solve2(1024)
}
println("T2: $t2")

data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)
}
