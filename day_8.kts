import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.time.measureTime

val size = 50
val antennas = mutableMapOf<Char, MutableList<Vec2>>()

/**
 * Another easy puzzle which is weird for day 8 IMO.
 * Solution1:
 * - Just find distance vector between two antennas.
 * - Add first anti node by adding the distance vector to the first antenna
 * - Add second anti node by subtracting the distance vector from the second antenna
 * - (Of course check the anti node coordinates for grid limits)
 * - Count them (in my case I put them into a set)
 * Solution2:
 * - On top of all the anti nodes from the part 1
 * - Find the minimal distance unit vector of the distance you calculated. (In
 * my case, I integrated into my custom Vec2 class as a method)
 * - As you go through possible antenna pairs as in part1, loop through the possible
 * anti node positions that you find by adding and subtracting the unit vector you
 * calculated previously.
 */
fun solve1and2() {
    val antiNodes = mutableSetOf<Vec2>()
    val antiNodes2 = mutableSetOf<Vec2>()
    antennas.forEach { (_, v) ->
        for (i in v.indices) {
            for (j in i + 1 until v.size) {
                val dist =
                    v[i] - v[j] // Find the actual distance between 2 antennas
                val mDist = dist
                //dist.minimal() // To find the unit (kind of) vector for part 2 (I thought this was necessary but obviously not)
                val an1 = v[i] + dist
                if (an1.x in 0 until size && an1.y in 0 until size) {
                    antiNodes.add(an1)
                    antiNodes2.add(an1)
                }
                var s1 = an1 + mDist
                while (s1.x in 0 until size && s1.y in 0 until size) {
                    antiNodes2.add(s1)
                    s1 += mDist
                }
                val an2 = v[j] - dist
                if (an2.x in 0 until size && an2.y in 0 until size) {
                    antiNodes.add(an2)
                    antiNodes2.add(an2)
                }
                var s2 = an2 - mDist
                while (s2.x in 0 until size && s2.y in 0 until size) {
                    antiNodes2.add(s2)
                    s2 -= mDist
                }
                antiNodes2.add(v[i]) // All the antenna pairs are effectively anti node in part2
                antiNodes2.add(v[j])
            }
        }
    }
    println("1: ${antiNodes.size}")
    println("2: ${antiNodes2.size}")
}

/**
 * Custom intuitive Vec2 class
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
    Files.readAllLines(Paths.get("input\\day8_input.txt"))
        .forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c != '.') {
                    if (!antennas[c].isNullOrEmpty()) {
                        antennas[c]!!.add(Vec2(x, y))
                    } else {
                        antennas[c] = mutableListOf(Vec2(x, y))
                    }
                }
            }
        }
}

val p = measureTime {
    readInput()
}

val t = measureTime {
    solve1and2()
}

println("P: $p")
println("T: $t")
