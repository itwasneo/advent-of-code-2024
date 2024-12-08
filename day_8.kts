import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.time.measureTime

var width: Int? = null
var height: Int? = null
val antennas = mutableMapOf<Char, MutableList<Vec2>>()

fun solve1() {
    val antiNodes = mutableSetOf<Vec2>()
    val antiNodes2 = mutableSetOf<Vec2>()
    antennas.forEach { (k, v) ->
        //println("Processing: $k")
        for (i in v.indices) {
            for (j in i + 1 until v.size) {
                val dist = v[i] - v[j]
                val mdist = dist.minimal()
                val an1 = v[i] + dist
                if (an1.x in 0 until width!! && an1.y in 0 until height!!) {
                    antiNodes.add(an1)
                    antiNodes2.add(an1)
                }
                var s1 = an1 + mdist
                while (s1.x in 0 until width!! && s1.y in 0 until height!!) {
                    antiNodes2.add(s1)
                    s1 += mdist
                }
                val an2 = v[j] - dist
                if (an2.x in 0 until width!! && an2.y in 0 until height!!) {
                    antiNodes.add(an2)
                    antiNodes2.add(an2)
                }
                var s2 = an2 - mdist
                while (s2.x in 0 until width!! && s2.y in 0 until height!!) {
                    antiNodes2.add(s2)
                    s2 -= mdist
                }
                //println("v1: ${v[i]}, v2: ${v[j]}, dist: $dist, an1: $an1, an2: $an2")
            }
        }
        if (v.size > 1) {
            antiNodes2.addAll(v)
        }
    }
    println("1: ${antiNodes.size}")
    println("2: ${antiNodes2.size}")
}

/**
 * Like the idea of proper Vec2
 */
data class Vec2(var x: Int, var y: Int) {
    operator fun plus(other: Vec2): Vec2 =
        Vec2(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vec2): Vec2 =
        Vec2(this.x - other.x, this.y - other.y)

    fun minimal(): Vec2 {
        val divisor = gcd(abs(x), abs(y)) // Use absolute values for GCD
        return Vec2(x / divisor, y / divisor)
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }
}

fun readInput() {
    Files.newInputStream(Paths.get("input\\day8_input.txt")).use {
        val br = BufferedReader(InputStreamReader(it))
        var line: String? = br.readLine()
        var y = 0
        width = line!!.length
        while (!line.isNullOrEmpty()) {
            line.forEachIndexed { x, c ->
                if (c != '.') {
                    if (!antennas[c].isNullOrEmpty()) {
                        antennas[c]!!.add(Vec2(x, y))
                    } else {
                        antennas[c] = mutableListOf(Vec2(x, y))
                    }
                }
                antennas
            }
            line = br.readLine()
            y += 1
        }
        height = y
    }
}

val t = measureTime {
    readInput()
    solve1()
}

println("T: $t")