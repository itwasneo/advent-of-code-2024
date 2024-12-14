import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.time.measureTime

//val w = 11
//val h = 7
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
            grid[p[1]][p[0]] += 1
            positions.add(Vec2(p[0], p[1]))
            moves.add(Vec2(v[0], v[1]))
            val horizontalMove = (v[0] * 100) % w
            val verticalMove = (v[1] * 100) % h
            val changeX = p[0] + horizontalMove
            val changeY = p[1] + verticalMove
            val finalX =
                if (changeX < 0) w + changeX else if (changeX >= w) changeX - w else changeX
            val finalY =
                if (changeY < 0) h + changeY else if (changeY >= h) changeY - h else changeY
            //println("p: $p, v: $v")
            Vec2(finalX, finalY)
        }.groupBy { x -> getQuadrant(x) }

    val result =
        qs.filter { (k, _) -> k != Q.EDGE }.values.fold(1) { acc, list -> acc * list.size }
    println("1: $result")
}

fun moveRobots() {
    positions.forEachIndexed { i, p ->
        val changeX = p.x + moves[i].x
        val changeY = p.y + moves[i].y
        val finalX =
            if (changeX < 0) w + changeX else if (changeX >= w) changeX - w else changeX
        val finalY =
            if (changeY < 0) h + changeY else if (changeY >= h) changeY - h else changeY
        positions[i] = Vec2(finalX, finalY)
        grid[p.y][p.x] -= 1
        grid[finalY][finalX] += 1
    }
}

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

fun render() {
    var cnt = 0

    repeat(10000) {
        //if (checkGrid()) break
        moveRobots()
        createPngFromArray(grid, "output\\Frame$cnt.png")
        cnt++
    }
}

fun checkGrid(): Boolean {
    return grid[h - 1].asList().none { it == 0 }
}

fun printGrid() {
    grid.forEach {
        it.forEach { c ->
            if (c == 0) print("#") else print(" ")
        }
        println("")
    }
}

val p = measureTime {
    readInput()
}
println("P: $p")

render()

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
