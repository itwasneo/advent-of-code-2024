import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val wireMap = mutableMapOf<String, Boolean>()
val notCalculated = mutableSetOf<Instruction>()

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

println(p)
println(t1)