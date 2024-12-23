import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

val size = 15
val grid = Array(size) { BooleanArray(size) { false } }
var s = Vec2(0, 0)
var e = Vec2(0, 0)
var sd = Vec2(0, 0)
val possibleCheats = mutableMapOf<Vec2, Pair<Vec2, Vec2>>()

val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
val dirs = listOf(right, down, left, up)

fun readInput() {
    Files.readAllLines(Paths.get("input\\day20_input_example.txt"))
        .forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[y][x] = char == '#'
                when (char) {
                    'S' -> {
                        s = Vec2(x, y)
                        sd = s - getNext(s, Vec2(1, 0))
                    }

                    'E' -> {
                        e = Vec2(x, y)
                    }
                }
            }
        }

    grid.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (c) {
                val pc = checkFourDirection(Vec2(x, y))
                if (pc != null) {
                    possibleCheats[pc.first] = pc.second
                }
            }
        }
    }
}

fun checkFourDirection(pos: Vec2): Pair<Vec2, Pair<Vec2, Vec2>>? {
    // Vertical
    val l = pos + left
    val r = pos + right
    if (checkVecInGrid(l) && checkVecInGrid(r) && !grid[l.y][l.x] && !grid[r.y][r.x]) {
        return pos to Pair(l, r)
    }

    // Horizontal
    val u = pos + up
    val d = pos + down
    if (checkVecInGrid(u) && checkVecInGrid(d) && !grid[u.y][u.x] && !grid[d.y][d.x]) {
        return pos to Pair(u, d)
    }
    return null
}

fun checkVecInGrid(pos: Vec2): Boolean {
    return pos.x in 0..<size && pos.y >= 0 && pos.y < size
}

fun solve1() {
    val track = buildTrack()
    var cnt = 0
    possibleCheats.forEach { cheat ->
        val l = track.indexOf(cheat.value.first)
        val r = track.indexOf(cheat.value.second)
        var cD: Int = if (l < r) {
            (r - l) - 2
        } else {
            (l - r) - 2
        }
        if (l != -1 && r != -1 && cD == 50) {
            cnt++
        }
    }
    println("1: $cnt")
}

fun dfs(
    p: Vec2,
    direction: Vec2,
    trackSet: Set<Vec2>,
    depth: Int,
    set: MutableSet<Vec2>
): Set<Vec2> {
    println("P: $p, D: $direction, T: $trackSet, DE: $depth, S: $set")
    val pos = p + direction
    if (depth == 20 || !checkVecInGrid(pos)) {
        return set
    }

    if (pos in trackSet) {
        set.add(pos)
    }
    dirs.forEach {
        set.addAll(dfs(pos, it, trackSet, depth + 1, set))
    }
    return set
}

fun solve2() {
    val track = buildTrack()
    val trackSet = track.toSet()

    var cnt = 0
    track.forEach { p ->
        println("P: $p")
        dirs.flatMap { dir ->
            dfs(p, dir, trackSet, 0, mutableSetOf())
        }.toSet().forEach {
            val l = track.indexOf(p)
            val r = track.indexOf(it)
            if (r > l) {
                var cD = r - l - 2
                if (cD == 50) {
                    cnt++
                }
            }
        }
    }
    println("2: $cnt")
}

fun buildTrack(): List<Vec2> {
    val track = LinkedList<Vec2>()
    track.add(s)
    while (true) {
        var current = track.last
        if (current == e) {
            break
        }

        if (current == s) {
            val next = getNext(current, sd)
            track.add(next)
            continue
        }
        val next = getNext(current, track.last - track[track.size - 2])
        track.addLast(next)
    }
    return track
}

fun getNext(current: Vec2, dir: Vec2): Vec2 {
    val next = current + dir
    if (!grid[next.y][next.x]) {
        return next
    } else {
        dirs.filter { it != oppositeDirection(dir) }.forEach {
            val newPos = current + it
            if (!grid[newPos.y][newPos.x]) {
                return newPos
            }
        }
    }
    throw RuntimeException("THIS SHOULDN'T HAPPEN")
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

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Int) = Vec2(x * scalar, y * scalar)
    operator fun div(scalar: Int) = Vec2(x / scalar, y / scalar)
    //fun manhattan() = Math.abs(x) + Math.abs(y)
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