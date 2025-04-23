import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.time.measureTime

private var codes: List<String> = emptyList()

private val numpad = mapOf(
    '7' to NP(0, 0), '8' to NP(1, 0), '9' to NP(2, 0),
    '4' to NP(0, 1), '5' to NP(1, 1), '6' to NP(2, 1),
    '1' to NP(0, 2), '2' to NP(1, 2), '3' to NP(2, 2),
    '_' to NP(0, 3), '0' to NP(1, 3), 'A' to NP(2, 3)
)

private val directionPad = mapOf(
    '^' to NP(1, 0), 'A' to NP(2, 0),
    '<' to NP(0, 1), 'v' to NP(1, 1), '>' to NP(2, 1)
)

fun readInput() {
    codes = Files.readAllLines(Paths.get("input/day21_input_example.txt"))
}

fun solve1() {
    codes.forEach { c ->
        val code = "A$c"
        val all1 = mutableListOf<String>()
        val i1 = code.windowed(2, 1, false) {
            moveFromToNumPad(it[0], it[1], "").map { m ->
                pressButton(m)
            }
        }

        val secondControllerMoves = StringBuilder()
        "A$firstControllerMoves".windowed(2, 1, false) {
            moveFromToDirectionPad(it[0], it[1], secondControllerMoves)
            pressButton(secondControllerMoves)
        }

        val thirdControllerMoves = StringBuilder()
        "A$secondControllerMoves".windowed(2, 1, false) {
            moveFromToDirectionPad(it[0], it[1], thirdControllerMoves)
            pressButton(thirdControllerMoves)
        }

        println(thirdControllerMoves.length)
    }
}

val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

println("P: $p")
println("T1: $t1")

data class NP(val x: Int, val y: Int)

private fun moveFromToNumPad(
    from: Char,
    to: Char,
    acc: String
): List<String> {
    val fromPos = numpad[from] ?: error("Invalid from")
    val toPos = numpad[to] ?: error("Invalid to")

    val rowDiff = toPos.y - fromPos.y
    val colDiff = toPos.x - fromPos.x
    var result1 = acc
    var result2 = acc

    repeat(abs(rowDiff)) {
        result1 += if (rowDiff > 0) 'v' else '^'
    }
    repeat(abs(colDiff)) {
        result1 += if (colDiff > 0) '>' else '<'
    }

    repeat(abs(rowDiff)) {
        result2 = if (rowDiff > 0) "v$result2" else "^$result2"
    }
    repeat(abs(colDiff)) {
        result2 = if (colDiff > 0) ">$result2" else "<$result2"
    }

    return listOf(result1, result2)
}

private fun moveFromToDirectionPad(
    from: Char,
    to: Char,
    acc: String
): List<String> {
    val fromPos = directionPad[from] ?: error("Invalid from")
    val toPos = directionPad[to] ?: error("Invalid to")

    val rowDiff = toPos.y - fromPos.y
    val colDiff = toPos.x - fromPos.x

    var result1 = acc
    var result2 = acc

    repeat(abs(rowDiff)) {
        result1 += if (rowDiff > 0) 'v' else '^'
    }
    repeat(abs(colDiff)) {
        result1 += if (colDiff > 0) '>' else '<'
    }

    repeat(abs(rowDiff)) {
        result2 = if (rowDiff > 0) "v$result2" else "^$result2"
    }
    repeat(abs(colDiff)) {
        result2 = if (colDiff > 0) ">$result2" else "<$result2"
    }

    return listOf(result1, result2)
}

private fun pressButton(acc: String): String {
    return "${acc}A"
}
