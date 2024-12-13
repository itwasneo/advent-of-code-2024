import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val fileName = "input\\day13_input.txt"
val clawMachines = mutableListOf<Pair<LongArray, LongArray>>()

fun readInput(part2: Boolean) {
    val txt = Files.readAllLines(Paths.get(fileName))
    var eq1 = LongArray(3) { -1 }
    var eq2 = LongArray(3) { -1 }
    txt.forEach { line ->
        if (line.isBlank() || line.isEmpty()) {
            clawMachines.add(Pair(eq1.copyOf(), eq2.copyOf()))
        } else if (line.startsWith("Button A:")) {
            val tmp = line.drop(10)
            val (x1, y1) = tmp.split(", ", limit = 2).map { it.drop(2) }
                .map(String::toLong)
            eq1[0] = x1
            eq2[0] = y1
        } else if (line.startsWith("Button B:")) {
            val tmp = line.drop(10)
            val (x2, y2) = tmp.split(", ", limit = 2).map { it.drop(2) }
                .map(String::toLong)
            eq1[1] = x2
            eq2[1] = y2
        } else {
            val tmp = line.drop(7)
            val (p1, p2) = tmp.split(", ", limit = 2).map { it.drop(2) }
                .map(String::toLong)
            eq1[2] = if (part2) p1 + 10000000000000 else p1
            eq2[2] = if (part2) p2 + 10000000000000 else p2
        }
    }
    clawMachines.add(Pair(eq1, eq2))

    /*
    clawMachines.forEach { cm ->
        println("1st Eq: ${cm.first.contentToString()}")
        println("2nd Eq: ${cm.second.contentToString()}")
        println("--------------------------------------")
    }
    */
}

fun solve1() {
    val result = clawMachines.sumOf { cm ->
        val lcm = lcm(
            cm.first[0],
            cm.second[0]
        ) // Finding Button A's Least Common Multiple
        val coef1 = lcm / cm.first[0]
        val coef2 = lcm / cm.second[0]
        val y1Tc1 = cm.first[1] * coef1
        val y2Tc2 = cm.second[1] * coef2
        val p1Tc1 = cm.first[2] * coef1
        val p2Tc2 = cm.second[2] * coef2
        if ((p2Tc2 - p1Tc1) % (y2Tc2 - y1Tc1) != 0L) {
            return@sumOf 0
        }
        val b = (p2Tc2 - p1Tc1) / (y2Tc2 - y1Tc1)

        if ((cm.second[2] - cm.second[1] * b) % cm.second[0] != 0L) {
            return@sumOf 0
        }
        val a = (cm.second[2] - cm.second[1] * b) / cm.second[0]
        if (a <= 0 || b <= 0) {
            0
        } else {
            a * 3 + b
        }
    }
    println("1: $result")
}

fun solve12() {
    val result = clawMachines.sumOf { cm ->
        val lcm = lcm(
            cm.first[1],
            cm.second[1]
        ) // Finding Button B's Least Common Multiple
        val coef1 = lcm / cm.first[1]
        val coef2 = lcm / cm.second[1]
        val x1Tc1 = cm.first[0] * coef1
        val x2Tc2 = cm.second[0] * coef2
        val p1Tc1 = cm.first[2] * coef1
        val p2Tc2 = cm.second[2] * coef2
        val a = (p2Tc2 - p1Tc1) / (x2Tc2 - x1Tc1)
        val b = (cm.first[2] - cm.first[0] * a) / cm.first[1]
        if (a > 100 || b > 100 || a <= 0 || b <= 0) {
            0
        } else {
            a * 3 + b
        }
    }
    println("1: $result")
}

private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

private fun lcm(a: Long, b: Long): Long {
    return (a / gcd(a, b)) * b
}

val p = measureTime {
    readInput(true)
}
println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")