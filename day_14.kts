import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.time.measureTime

/**
 * Well since there was no clear explanation about how the "Christmas tree" should
 * look like, first I solve the second part with brute force. I created pngs for
 * each frame and look through them to find a tree shaped struct.
 *
 * After seeing how the tree look like I realized that checking for 10 consecutive
 * non-zero integers in my grid horizontally each frame would result in the right
 * answer.
 */
val w = 101
val h = 103
val mw = (w / 2)
val mh = (h / 2)
val grid = Array(h) { IntArray(w) { 0 } }
val positions = mutableListOf<Vec2>()
val moves = mutableListOf<Vec2>()

fun readInput() {

    val qs = Files.readAllLines(Paths.get("input\\day14_input.txt"))
        .map { l ->
            val (p, v) = l.split(" ", limit = 2)
                .map { x: String ->
                    x.drop(2).split(",", limit = 2).map(String::toInt)
                }
            grid[p[1]][p[0]] += 1 // Creating the grid (Every non-zero cell represents one or more robots)
            positions.add(Vec2(p[0], p[1]))
            moves.add(Vec2(v[0], v[1]))
            val horizontalMove =
                (v[0] * 100) % w // Calculating the total horizontal move after 100 steps
            val verticalMove =
                (v[1] * 100) % h // Calculating the total vertical move after 100 steps
            val finalX = wrapCoordinate(p[0] + horizontalMove, w)
            val finalY = wrapCoordinate(p[1] + verticalMove, h)
            Vec2(finalX, finalY)
        }.groupBy { x -> getQuadrant(x) }

    val result =
        qs.filter { (k, _) -> k != Q.EDGE }.values.fold(1) { acc, list -> acc * list.size }
    println("1: $result")
}

/**
 * Moves each robot inside the grid, 1 step according to their velocities
 */
fun moveRobots() {
    positions.forEachIndexed { i, p ->
        val changeX = wrapCoordinate(p.x + moves[i].x, w)
        val changeY = wrapCoordinate(p.y + moves[i].y, h)
        positions[i] = Vec2(changeX, changeY)
        grid[p.y][p.x] -= 1
        grid[changeY][changeX] += 1
    }
}

/**
 * Utility function to clamp x and y values
 */
fun wrapCoordinate(value: Int, max: Int): Int = (value + max) % max

/**
 * Simulates robot movements until grid check successful
 */
fun solve2() {
    var cnt = 1 // ;)
    while (true) {
        moveRobots()
        //createPngFromArray(grid, "output\\Frame$cnt.png")
        if (checkGrid()) {
            //printGrid()
            break
        }
        cnt++
    }
    println("2: $cnt")
}

/**
 * Checks for 10 consecutive non-zero cells
 */
fun checkGrid(): Boolean {
    grid.forEach {
        var count = 0
        for (num in it) {
            if (num != 0) {
                count++
                if (count == 10) return true
            } else {
                count = 0
            }
        }
    }
    return false
}

val p = measureTime {
    readInput()
}
println("P: $p")

val t2 = measureTime {
    solve2()
}
println("T2: $t2")

enum class Q {
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    EDGE
}

fun getQuadrant(v: Vec2): Q {
    val (x, y) = v
    return if (x in 0 until mw && y in 0 until mh) {
        Q.FIRST
    } else if (x in mw + 1 until w && y in 0 until mh) {
        Q.SECOND
    } else if (x in 0 until mw && y in mh + 1 until h) {
        Q.THIRD
    } else if (x in mw + 1 until w && y in mh + 1 until h) {
        Q.FOURTH
    } else {
        Q.EDGE
    }
}

data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)

    override fun toString(): String {
        return "($x - $y)"
    }

}

fun printGrid() {
    grid.forEach {
        it.forEach { c ->
            if (c == 0) print("#") else print(" ")
        }
        println("")
    }
}

/**
 * Used during brute force for part 2. Creates a png file for the grid
 * White pixels represent robots
 */
fun createPngFromArray(array: Array<IntArray>, outputPath: String) {
    // Get dimensions of the array
    val height = h
    val width = w

    // Create a BufferedImage
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    // Set pixels based on the array
    for (y in array.indices) {
        for (x in array[y].indices) {
            val color =
                if (array[y][x] == 0) Color.BLACK.rgb else Color.WHITE.rgb
            image.setRGB(x, y, color)
        }
    }

    // Write the image to a file
    val outputFile = File(outputPath)
    ImageIO.write(image, "png", outputFile)
}
