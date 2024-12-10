import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

/**
 * Another easy one that needs no explanation. This year's problems waay easier
 * than the last year's
 */

data class Vec2(var x: Int, var y: Int)

val size = 47
var g: Array<IntArray> = Array(size) { IntArray(size) { -1 } }
val dirs = arrayOf(Vec2(1, 0), Vec2(0, 1), Vec2(-1, 0), Vec2(0, -1))
val zeros = mutableListOf<Vec2>()

fun solve1() {
    val result = zeros.sumOf { walk(it, 0)?.size ?: 0 }
    //val result = zeros.sumOf { walkWithStack(it, 0) }
    println("1: $result")
}

fun solve2() {
    val result = zeros.sumOf { walkWithCounts(it, 0) }
    println("2: $result")
}

/**
 * This runs twice as much slower than recursion :(, but it was nice to see I
 * didn't forget how to solve it not using recursion
 */
fun walkWithStack(pos: Vec2, target: Int): Int {
    val stack = Stack<Pair<Vec2, Int>>()
    val visited = mutableSetOf<Vec2>()
    val foundEnds = mutableSetOf<Vec2>()

    stack.push(Pair(pos, target))
    while (stack.isNotEmpty()) {
        val (p, t) = stack.pop()
        if (p.x !in 0 until size || p.y !in 0 until size) {
            continue
        }

        if (g[p.y][p.x] != t) {
            continue
        }

        if (t == 9) {
            foundEnds.add(p)
        } else {
            visited.add(p)
            dirs.forEach {
                val nextPosition =
                    Vec2(p.x + it.x, p.y + it.y)
                if (nextPosition !in visited) {
                    stack.add(Pair(nextPosition, t + 1))
                }
            }
        }
    }
    return foundEnds.size
}

fun walk(currentPosition: Vec2, target: Int): Set<Vec2>? {
    if (!(currentPosition.x in 0..<size && currentPosition.y in 0..<size)) {
        return null
    }
    if (g[currentPosition.y][currentPosition.x] != target) {
        return null
    }
    return if (target == 9) {
        setOf(currentPosition)
    } else {
        val uniqueEnds = mutableSetOf<Vec2>()
        dirs.forEach { dir ->
            val nextPosition = Vec2(
                currentPosition.x + dir.x,
                currentPosition.y + dir.y
            )
            walk(nextPosition, target + 1)?.let { uniqueEnds.addAll(it) }
        }
        return uniqueEnds
    }
}

fun walkWithCounts(
    currentPosition: Vec2,
    target: Int
): Int {
    if (!(currentPosition.x in 0..<size && currentPosition.y in 0..<size)) {
        return 0
    }
    if (g[currentPosition.y][currentPosition.x] != target) {
        return 0
    }
    return if (target == 9) {
        1
    } else {
        dirs.sumOf { dir ->
            val nextPosition = Vec2(
                currentPosition.x + dir.x,
                currentPosition.y + dir.y
            )
            walkWithCounts(nextPosition, target + 1)
        }
    }
}

fun readInput() {
    Files.readAllLines(Paths.get("input\\day10_input.txt"))
        .forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    g[y][x] = c.digitToInt()
                    if (c == '0') zeros.add(Vec2(x, y))
                } else {
                    g[y][x] = -1
                }
            }
        }
}

ManualClassLoader.load()
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

/**
 * Dummy class to warm-up JVM, before running the solution operations
 */
class Dummy {
    fun m() {
    }
}

/**
 * Utility to warm-up JVM
 */
object ManualClassLoader {
    internal fun load() {
        for (i in 0..99999) {
            val dummy = Dummy()
            dummy.m()
        }
    }
}
