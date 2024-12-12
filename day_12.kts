import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.time.measureTime

/**
 * Finally a good question. For part 1, I used a classic stack based path finding
 * algorithm to extract each field as Set of Vec2s. In order to calculate the
 * perimeters, I iterate each field cell and count how many adjacent cell it has
 * and then subtract it from 4. Area is basically the size of field Sets.
 *
 * For part 2, I converted each cell in a field into "corners". Then count the
 * unique points. If a corner is shared by 1 or 3 cells, that means it is a corner
 * on the actual field. 1 edge case is the corners shared by 2 cells but "diagonally"
 * (I handled that case exclusively). At the end, number of corners mean number
 * of sides, easy :)
 *
 *      *--*
 *      |xy|
 *      *--*
 */
val size = 140
val input = Array(size) { CharArray(size) { ' ' } }
val fields = mutableListOf<Set<Vec2>>()
val dirs = listOf(
    Vec2(1, 0),
    Vec2(0, 1),
    Vec2(-1, 0),
    Vec2(0, -1)
) // RIGHT - DOWN - LEFT - UP

fun solve1() {
    val visitedGridCells = mutableSetOf<Vec2>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (Vec2(x, y) !in visitedGridCells) {
                val newField = walkSingleField(c, Vec2(x, y))
                fields.add(newField)
                visitedGridCells.addAll(newField)
            }
        }
    }
    val result = fields.sumOf { f ->
        f.size * calculatePerimeter(f)
    }
    println("1: $result")
}

fun solve2() {
    val result = fields.sumOf { f ->
        val numberOfSides = calculateNumberOfSides(f)
        f.size * numberOfSides
    }
    println("2: $result")
}

fun calculatePerimeter(field: Set<Vec2>): Int {
    var perimeter = 0
    field.forEach { cell ->
        var numberOfAdj = 0
        dirs.forEach { dir ->
            if (cell + dir in field) numberOfAdj++
        }
        perimeter += 4 - numberOfAdj // To calculate each cells contribution to the perimeter
    }
    return perimeter
}

fun calculateNumberOfSides(field: Set<Vec2>): Int {
    val corners = mutableMapOf<Vec2, Int>()
    field.forEach { cell -> // Each cell potentially has 4 corners
        listOf(
            cell,
            cell + dirs[0],
            cell + Vec2(1, 1),
            cell + dirs[1]
        ).forEach { currentCorner ->
            corners[currentCorner] =
                corners.getOrPut(currentCorner) { 0 } + 1 // Count have many unique cells shares this corner
        }
    }
    var result = 0
    corners.forEach { (k, v) ->
        if (v == 1 || v == 3) {
            result++
        } else if (v == 2 && hasDiagonal(
                k,
                field
            )
        ) { // This is the special case with diagonally shared corner by 2 cells.
            result += 2 // !!!IMPORTANT!!! not increase by 1
        }
    }
    return result
}

fun hasDiagonal(pos: Vec2, field: Set<Vec2>): Boolean {
    return (pos in field && pos + Vec2(
        -1,
        -1
    ) in field) || (pos + dirs[3] in field && pos + dirs[2] in field)
}

// Classic path finder algorithm
fun walkSingleField(currentField: Char, start: Vec2): Set<Vec2> {
    val seen = mutableSetOf<Vec2>()
    val path = Stack<Vec2>()
    val field = mutableSetOf<Vec2>()
    path.push(start)

    while (path.isNotEmpty()) {
        val pos = path.pop()

        if (pos.x !in 0 until size || pos.y !in 0 until size) {
            continue
        }

        seen.add(pos)
        if (input[pos.y][pos.x] == currentField) {
            field.add(pos)
        } else {
            continue
        }

        dirs.forEach {
            val nextPos = pos + it
            if (nextPos !in seen) path.add(nextPos)
        }

    }
    return field
}


fun readInput() {
    Files.readAllLines(Paths.get("input\\day12_input.txt"))
        .forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                input[y][x] = c
            }
        }
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

data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)

    override fun toString(): String {
        return "$x - $y"
    }
}
