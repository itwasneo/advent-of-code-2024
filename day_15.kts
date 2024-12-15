import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val size = 10
val filename = "input\\day15_input_example.txt"
val grid = Array(size) { Array(size) { Cell.F } }
var tGrid = Array(size) { Array(size) { Cell.F } }
var grid2 = Array(size) { Array(size * 2) { Cell.F } }
val moves = mutableListOf<Vec2>()
val right = Vec2(1, 0)
val down = Vec2(0, 1)
val left = Vec2(-1, 0)
val up = Vec2(0, -1)
var start: Vec2? = null

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

    /*
    grid.forEach {
        it.forEach { c ->
            print(c)
        }
        println("")
    }
    println("====================================")

    grid2.forEach {
        it.forEach { c ->
            print(c)
        }
        println("")
    }
    println("====================================")

     */

    /*
    tGrid.forEach {
        println(it.contentToString())
    }

     */
}

fun solve1() {
    var result = 0
    var currentPosition = start!!
    moves.forEach { move ->
        //println("APPLYING $move")
        currentPosition = applyMove(currentPosition, move)
        /*
        grid.forEach {
            it.forEach { c ->
                print(c)
            }
            println("")
        }
        println("==================================================")
         */
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
    return if (grid[n.y][n.x] == Cell.F) {
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


val p = measureTime {
    readInput()
}
println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")




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

data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)

    override fun toString(): String {
        if (x == 1 && y == 0) {
            return "RIGHT"
        } else if (x == 0 && y == 1) {
            return "DOWN"
        } else if (x == -1 && y == 0) {
            return "LEFT"
        } else if (x == 0 && y == -1) {
            return "UP"
        } else {
            return "($x - $y)"
        }
    }

}
