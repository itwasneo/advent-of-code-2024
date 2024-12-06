import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val size = 130
val rm = mutableSetOf<Pair<Int, Int>>()
val om = Array(size) { BooleanArray(size) { false } }
var st: Pair<Int, Int>? = null
fun Pair<Int, Int>.next(): Pair<Int, Int> {
    return when (this) {
        Pair(0, -1) -> Pair(1, 0)
        Pair(1, 0) -> Pair(0, 1)
        Pair(0, 1) -> Pair(-1, 0)
        Pair(-1, 0) -> Pair(0, -1)
        else -> throw RuntimeException()
    }
}

fun readInput() {
    Files.readAllLines(Paths.get("input\\day6_input.txt"))
        .forEachIndexed { i, l ->
            l.trim().forEachIndexed { j, c ->
                when (c) {
                    '.' -> {
                        // SKIP
                    }

                    '#' -> {
                        om[i][j] = true
                    }

                    else -> {
                        rm.add(Pair(j, i))
                        st = Pair(j, i)
                    }
                }
            }
        }
}

fun solve1() {
    var x = st!!.first
    var y = st!!.second
    var dir = Pair(0, -1)
    while (x in om[0].indices && y in om.indices) {
        while (om[y][x]) {
            x -= dir.first
            y -= dir.second
            dir = dir.next()
            x += dir.first
            y += dir.second
        }
        rm.add(Pair(x, y))
        x += dir.first
        y += dir.second
    }
    val result = rm.size
    println("1: $result")
}

fun checkCyclic(): Boolean {
    var x = st!!.first
    var y = st!!.second
    var dir = Pair(0, -1)
    val edges = mutableSetOf<Pair<Int, Int>>()
    while (x in om.indices && y in om[0].indices) {
        if (om[y][x]) {
            x -= dir.first
            y -= dir.second
            dir = dir.next()
        }
        val e = Pair(to1D(x - dir.first, y - dir.second), to1D(x, y))
        if (edges.contains(e)) return true
        edges.add(e)
        x += dir.first
        y += dir.second
    }
    return false
}

fun to1D(x: Int, y: Int): Int = x * size + y

fun solve2() {
    var result = 0
    rm.forEach { p ->
        om[p.second][p.first] = true
        if (checkCyclic()) {
            result++
        }
        om[p.second][p.first] = false
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
