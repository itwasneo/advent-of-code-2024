import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * This solution has by far the worst implementation. I'm not happy with it, but
 * on the other hand, it is pretty straightforward. For part 1, I used the transpose
 * of the grid in order to find the first empty cell in the current column for the
 * relevant movement.
 *
 * For part 2 for vertical movement, I used 2 recursive functions first to determine
 * whether it is possible move vertically and second to move the boxes. Horizontal
 * movement was relatively easier, but this time I had to "flip" each box side
 * along the way.
 */

val size = 50
val filename = "input\\day15_input.txt"
val grid = Array(size) { Array(size) { Cell.F } }
var tGrid = Array(size) { Array(size) { Cell.F } }
var grid2 = Array(size) { Array(size * 2) { Cell.F } }
val moves = mutableListOf<Vec2>()
val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
var start: Vec2? = null
var start2: Vec2? = null

fun solve1() {
    var result = 0
    var currentPosition = start!!
    moves.forEach { move ->
        currentPosition = applyMove(currentPosition, move)
    }
    grid.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (c == Cell.B) result += 100 * y + x
        }
    }
    println("1: $result")
}

fun applyMove(currentPosition: Vec2, move: Vec2): Vec2 {
    val n = currentPosition + move
    if (grid[n.y][n.x] == Cell.F) {
        grid[currentPosition.y][currentPosition.x] = Cell.F
        grid[n.y][n.x] = Cell.T
        tGrid[currentPosition.x][currentPosition.y] = Cell.F
        tGrid[n.x][n.y] = Cell.T
        return n
    } else if (grid[n.y][n.x] == Cell.W) {
        return currentPosition
    } else if (grid[n.y][n.x] == Cell.B) {
        when (move) {
            right -> {
                val s = n.x
                val e =
                    (s..size).first { grid[n.y][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s..e).firstOrNull { grid[n.y][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                grid[n.y][firstEmptyCell] = Cell.B
                grid[n.y][n.x] = Cell.T
                grid[currentPosition.y][currentPosition.x] = Cell.F
                tGrid[firstEmptyCell][n.y] = Cell.B
                tGrid[n.x][n.y] = Cell.T
                tGrid[currentPosition.x][currentPosition.y] = Cell.F
                return n
            }

            down -> {
                val s = n.y
                val e =
                    (s..size).first { tGrid[n.x][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s..e).firstOrNull { tGrid[n.x][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                tGrid[n.x][firstEmptyCell] = Cell.B
                tGrid[n.x][n.y] = Cell.T
                tGrid[currentPosition.x][currentPosition.y] = Cell.F
                grid[firstEmptyCell][n.x] = Cell.B
                grid[n.y][n.x] = Cell.T
                grid[currentPosition.y][currentPosition.x] = Cell.F
                return n
            }

            left -> {
                val s = n.x
                val e =
                    (s downTo 0).first { grid[n.y][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s downTo e).firstOrNull { grid[n.y][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                grid[n.y][firstEmptyCell] = Cell.B
                grid[n.y][n.x] = Cell.T
                grid[currentPosition.y][currentPosition.x] = Cell.F
                tGrid[firstEmptyCell][n.y] = Cell.B
                tGrid[n.x][n.y] = Cell.T
                tGrid[currentPosition.x][currentPosition.y] = Cell.F
                return n
            }

            up -> {
                val s = n.y
                val e =
                    (s downTo 0).first { tGrid[n.x][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s downTo e).firstOrNull { tGrid[n.x][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                tGrid[n.x][firstEmptyCell] = Cell.B
                tGrid[n.x][n.y] = Cell.T
                tGrid[currentPosition.x][currentPosition.y] = Cell.F
                grid[firstEmptyCell][n.x] = Cell.B
                grid[n.y][n.x] = Cell.T
                grid[currentPosition.y][currentPosition.x] = Cell.F
                return n
            }

            else -> throw RuntimeException("UNKNOWN MOVE")
        }
    } else {
        throw RuntimeException("FATAL ERROR : ${grid[n.y][n.x]}") // Merge this branch with the above if no exception thrown
    }
}

fun solve2() {
    var result = 0
    var currentPosition = start2!!
    moves.forEach { move ->
        currentPosition = applyMove2(currentPosition, move)
    }
    grid2.forEachIndexed { y, r ->
        r.forEachIndexed { x, c ->
            if (c == Cell.L) result += 100 * y + x
        }
    }
    println("2: $result")
}

fun applyMove2(currentPosition: Vec2, move: Vec2): Vec2 {
    val n = currentPosition + move
    if (grid2[n.y][n.x] == Cell.F) {
        grid2[currentPosition.y][currentPosition.x] = Cell.F
        grid2[n.y][n.x] = Cell.T
        return n
    } else if (grid2[n.y][n.x] == Cell.W) {
        return currentPosition
    } else if (grid2[n.y][n.x] == Cell.L && move != left || grid2[n.y][n.x] == Cell.R && move != right) {
        when (move) {
            right -> {
                val s = n.x
                val e =
                    (s..size * 2).first { grid2[n.y][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s..e).firstOrNull { grid2[n.y][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                grid2[currentPosition.y][currentPosition.x] = Cell.F
                grid2[n.y][firstEmptyCell] = Cell.R
                (s + 1..<firstEmptyCell).forEach {
                    grid2[n.y][it] = flipBox(grid2[n.y][it])
                }
                grid2[n.y][n.x] = Cell.T
                return n
            }

            left -> {
                val s = n.x
                val e =
                    (s downTo 0).first { grid2[n.y][it] == Cell.W } // Finding first Wall, there must be at least 1
                val firstEmptyCell =
                    (s downTo e).firstOrNull { grid2[n.y][it] == Cell.F }
                if (firstEmptyCell == null) {
                    return currentPosition
                }
                grid2[currentPosition.y][currentPosition.x] = Cell.F
                grid2[n.y][firstEmptyCell] = Cell.L
                (firstEmptyCell + 1..<s).forEach {
                    grid2[n.y][it] = flipBox(grid2[n.y][it])
                }
                grid2[n.y][n.x] = Cell.T
                return n
            }

            up -> {
                if (grid2[n.y][n.x] == Cell.L &&
                    canGoVertical(n, n + right, up)
                ) {
                    goVertical(n, n + right, up)
                    grid2[currentPosition.y][currentPosition.x] = Cell.F
                    grid2[n.y][n.x + 1] = Cell.F
                    grid2[n.y][n.x] = Cell.T
                    return n
                } else if (grid2[n.y][n.x] == Cell.R &&
                    canGoVertical(n + left, n, up)
                ) {
                    goVertical(n + left, n, up)
                    grid2[currentPosition.y][currentPosition.x] = Cell.F
                    grid2[n.y][n.x - 1] = Cell.F
                    grid2[n.y][n.x] = Cell.T
                    return n
                } else {
                    return currentPosition
                }
            }

            down -> {
                if (grid2[n.y][n.x] == Cell.L
                    && canGoVertical(n, n + right, down)
                ) {
                    goVertical(n, n + right, down)
                    grid2[currentPosition.y][currentPosition.x] = Cell.F
                    grid2[n.y][n.x + 1] = Cell.F
                    grid2[n.y][n.x] = Cell.T
                    return n
                } else if (grid2[n.y][n.x] == Cell.R &&
                    canGoVertical(n + left, n, down)
                ) {
                    goVertical(n + left, n, down)
                    grid2[currentPosition.y][currentPosition.x] = Cell.F
                    grid2[n.y][n.x - 1] = Cell.F
                    grid2[n.y][n.x] = Cell.T
                    return n
                } else {
                    return currentPosition
                }
            }

            else -> throw RuntimeException("UNKNOWN MOVE")
        }
    } else {
        throw RuntimeException("FATAL ERROR : ${grid2[n.y][n.x]}")
    }
}

fun goVertical(l: Vec2, r: Vec2, d: Vec2) {
    when {
        grid2[l.y][l.x] == Cell.L && grid2[r.y][r.x] == Cell.R -> {
            goVertical(l + d, r + d, d)
        }

        grid2[l.y][l.x] == Cell.R && grid2[r.y][r.x] == Cell.L -> {
            goVertical(l + d + left, l + d, d)
            goVertical(r + d, r + d + right, d)
        }

        grid2[r.y][r.x] == Cell.L -> goVertical(r + d, r + d + right, d)
        grid2[l.y][l.x] == Cell.R -> goVertical(l + d + left, l + d, d)
    }

    val aboveLeft = grid2[l.y - d.y][l.x]
    val aboveRight = grid2[r.y - d.y][r.x]

    if (aboveLeft == Cell.T || aboveRight == Cell.T) {
        if (aboveLeft == Cell.T) grid2[l.y - d.y][l.x] = Cell.F
        if (aboveRight == Cell.T) grid2[r.y - d.y][r.x] = Cell.F
    } else {
        grid2[l.y - d.y][l.x] = Cell.F
        grid2[r.y - d.y][r.x] = Cell.F
    }

    grid2[l.y][l.x] = Cell.L
    grid2[r.y][r.x] = Cell.R
}

fun canGoVertical(l: Vec2, r: Vec2, d: Vec2): Boolean {
    val leftCell = grid2[l.y][l.x]
    val rightCell = grid2[r.y][r.x]

    return when {
        leftCell == Cell.F && rightCell == Cell.F -> true
        leftCell == Cell.W || rightCell == Cell.W -> false
        leftCell == Cell.L && rightCell == Cell.R -> {
            canGoVertical(l + d, r + d, d)
        }

        leftCell == Cell.R && rightCell == Cell.L -> {
            canGoVertical(l + d + left, l + d, d) &&
                    canGoVertical(r + d, r + d + right, d)
        }

        leftCell == Cell.R -> canGoVertical(l + d + left, l + d, d)
        rightCell == Cell.L -> canGoVertical(r + d, r + d + right, d)
        else -> throw RuntimeException("THIS SHOULDN'T HAPPEN")
    }
}


fun readInput() {
    val lines = Files.readAllLines(Paths.get(filename))
    for (y in 0 until size) {
        val l = lines[y]
        for (x in 0 until size * 2 step 2) {
            when (val c = l[x / 2]) {
                '#' -> {
                    grid[y][x / 2] = Cell.W
                    grid2[y][x] = Cell.W
                    grid2[y][x + 1] = Cell.W
                }

                'O' -> {
                    grid[y][x / 2] = Cell.B
                    grid2[y][x] = Cell.L
                    grid2[y][x + 1] = Cell.R
                }

                '@' -> {
                    grid[y][x / 2] = Cell.T
                    grid2[y][x] = Cell.T
                    grid2[y][x + 1] = Cell.F
                    start = Vec2(x / 2, y)
                    start2 = Vec2(x, y)
                }

                '.' -> {
                    grid[y][x / 2] = Cell.F
                    grid2[y][x] = Cell.F
                    grid2[y][x + 1] = Cell.F
                }

                else -> throw RuntimeException("WRONG INPUT: $c")

            }
        }
    }

    for (y in size + 1 until lines.size) {
        lines[y].forEach { c ->
            when (c) {
                '>' -> moves.add(right)
                'v' -> moves.add(down)
                '<' -> moves.add(left)
                '^' -> moves.add(up)
            }
        }
    }

    tGrid =
        Array(size) { col -> Array(size) { row -> grid[row][col] } }
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

// Utilities -------------------------------------------------------
enum class Cell {
    W {
        override fun toString(): String = "#"
    },
    F {
        override fun toString(): String = "."
    },
    B {
        override fun toString(): String = "O"
    },
    L {
        override fun toString(): String = "["
    },
    R {
        override fun toString(): String = "]"
    },
    T {
        override fun toString(): String = "@"
    };

    abstract override fun toString(): String
}

fun flipBox(boxSide: Cell): Cell {
    return when (boxSide) {
        Cell.L -> Cell.R
        Cell.R -> Cell.L
        else -> throw RuntimeException(
            "CAN'T FLIP : $boxSide"
        )
    }
}

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
