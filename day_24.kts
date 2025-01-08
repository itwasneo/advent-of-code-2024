import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val wireMap = mutableMapOf<String, Boolean>()
val notCalculated = mutableSetOf<Instruction>()
val resultMap = mutableMapOf<String, Instruction>()

fun readInput() {
    var checkingWires = true
    Files.readAllLines(Paths.get("input/day24_input.txt"))
        .forEach { line ->
            if (line.isNullOrEmpty()) {
                checkingWires = false
            } else if (checkingWires) {
                val (wire, value) = line.split(": ")
                wireMap[wire] = value == "1"
            } else {
                val tokens = line.split(" ")
                val wire1 = tokens.first()
                val wire2 = tokens[2]
                val oper = tokens[1]
                val resultWire = tokens.last()
                if (wireMap.containsKey(wire1) && wireMap.containsKey(wire2)) {
                    val result = when (oper) {
                        "AND" -> wireMap[wire1]!! && wireMap[wire2]!!
                        "OR" -> wireMap[wire1]!! || wireMap[wire2]!!
                        "XOR" -> wireMap[wire1]!! xor wireMap[wire2]!!
                        else -> error("Unknown operator")
                    }
                    wireMap[resultWire] = result
                } else {
                    notCalculated.add(
                        Instruction(
                            wire1,
                            wire2,
                            oper,
                            resultWire
                        )
                    )
                }
                resultMap[resultWire] = Instruction(
                    wire1,
                    wire2,
                    oper,
                    resultWire
                )
            }
        }
}

fun solve1() {
    while (notCalculated.isNotEmpty()) {
        val toRemove = mutableSetOf<Instruction>()
        notCalculated.forEach { instr ->
            if (wireMap.containsKey(instr.wire1) && wireMap.containsKey(instr.wire2)) {
                val result = when (instr.oper) {
                    "AND" -> wireMap[instr.wire1]!! && wireMap[instr.wire2]!!
                    "OR" -> wireMap[instr.wire1]!! || wireMap[instr.wire2]!!
                    "XOR" -> wireMap[instr.wire1]!! xor wireMap[instr.wire2]!!
                    else -> error("Unknown operator")
                }
                wireMap[instr.resultWire] = result
                toRemove.add(instr)
            }
        }
        notCalculated.removeAll(toRemove)
    }
    val sortedKeys =
        wireMap.filter { it.key.startsWith("z") }.keys.sorted()
    var result = 0L
    for ((index, key) in sortedKeys.withIndex()) {
        if (wireMap[key] == true) {
            result = result or (1L shl index)
        }
    }
    println("1: $result")
}

fun solve2() {
    val x = createNumber("x")
    val y = createNumber("y")
    val z = getBits("z")

    val xPlusy = x + y
    val xPlusyBits = convertToBooleanList(xPlusy)

    val notMatchingIndices = mutableListOf<Int>()
    for (i in z.indices) {
        if (z[i] != xPlusyBits[i]) {
            notMatchingIndices.add(i)
        }
    }
    println("Indices: $notMatchingIndices")
    z.forEach { if (it) print("1") else print("0") }
    println()
    xPlusyBits.forEach { if (it) print("1") else print("0") }
    println()
    notMatchingIndices.forEach { num ->
        val wire = resultMap.filter {
            it.key.startsWith("z") && it.key.endsWith(
                String.format("%02d", num)
            )
        }
    }
}

fun getBits(wirePrefix: String): List<Boolean> {
    val keys = wireMap.filter { it.key.startsWith(wirePrefix) }.keys.sorted()
    return keys.map { wireMap[it]!! }
}

fun createNumber(wirePrefix: String): Long {
    val bits = getBits(wirePrefix)
    var result = 0L
    for ((index, bit) in bits.withIndex()) {
        if (bit) {
            result = result or (1L shl index)
        }
    }
    return result
}

fun convertToBooleanList(number: Long): List<Boolean> {
    val bits = mutableListOf<Boolean>()
    var n = number
    while (n > 0) {
        bits.add(n % 2 == 1L)
        n /= 2
    }
    return bits
}

data class Instruction(
    val wire1: String,
    val wire2: String,
    val oper: String,
    val resultWire: String
)

val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

val t2 = measureTime {
    solve2()
}

println(p)
println(t1)
println(t2)