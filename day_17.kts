import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

/**
 * Very fun question :) neo approved
 */

// Registers
var ra: Long? = null
var rb: Long? = null
var rc: Long? = null

// Instruction Pointer
var ip: Int? = 0 // Increases by 2 after each instructions (opcode + operand)

// Output
var ou: MutableList<Int> = mutableListOf()

// Program
var pr: List<Int>? = null

enum class Operand {
    LITERAL,
    COMBO
}

fun getOperandValue(operand: Operand, id: Int): Long {
    return when (operand) {
        Operand.LITERAL -> id.toLong()
        Operand.COMBO -> when (id) {
            0, 1, 2, 3 -> id.toLong()
            4 -> ra!!
            5 -> rb!!
            6 -> rc!!
            else -> throw RuntimeException("Unknown operand value: $id")
        }
    }
}

// Instructions

/**
 *  adv (division) truncates (rounding to zero) the result to an integer. Writes
 *  the result into register A
 *  opcode: 0
 *  numerator:      ra
 *  denominator:    operand(COMBO)
 */
fun adv(operand: Int) {
    ra =
        ra!! shr getOperandValue( // dividing a number to power of 2 -> shifting right
            Operand.COMBO,
            operand
        ).toInt() // by default rounds to zero
    jump()
}

/**
 * bxl (bitwise XOR) Writes the result to register B
 * opcode: 1
 * left:    rb
 * right:   operand(LITERAL)
 */
fun bxl(operand: Int) {
    rb = rb!! xor getOperandValue(Operand.LITERAL, operand)
    jump()
}

/**
 * bst (modulo 8) Writes the result to register B.
 * opcode: 2
 * input:   operand(COMBO)
 */
fun bst(operand: Int) {
    rb = getOperandValue(
        Operand.COMBO,
        operand
    ) and 7 // Taking last 3 bits by bitwise and with 7
    jump()
}

/**
 * jnz (jump) Does nothing if register A is zero. If it is not zero, "moves" the
 * instruction pointer to the operand LITERAL value. If this instruction makes
 * instrunction pointer jump, instruction pointers DOES NOT increase by 2 after.
 * opcode: 3
 * input: operand(LITERAL)
 */
fun jnz(operand: Int) {
    if (ra!! != 0L) ip =
        getOperandValue(Operand.LITERAL, operand).toInt() else jump()
}

/**
 * bxc (bitwise XOR) Write the result to register B. Operand is not used!!
 * opcode: 4
 * left: rb
 * right: rc
 */
fun bxc() {
    rb = rb!! xor rc!!
    jump()
}

/**
 * out (print) Outputs the operand COMBO modulo 8.
 * opcode: 5
 * input: operand(COMBO)
 */
fun out(operand: Int): Int {
    val result = getOperandValue(Operand.COMBO, operand) and 7
    ou.add(result.toInt())
    jump()
    return result.toInt()
}

/**
 *  bdv (division) truncates (rounding to zero) the result to an integer. Writes
 *  the result into register B
 *  opcode: 6
 *  numerator:      ra
 *  denominator:    operand(COMBO)
 */
fun bdv(operand: Int) {
    rb =
        ra!! shr getOperandValue( // dividing a number to power of 2 -> shifting right
            Operand.COMBO,
            operand
        ).toInt() // by default rounds to zero
    jump()
}

/**
 *  cdv (division) truncates (rounding to zero) the result to an integer. Writes
 *  the result into register C
 *  opcode: 7
 *  numerator:      ra
 *  denominator:    operand(COMBO)
 */
fun cdv(operand: Int) {
    rc =
        ra!! shr getOperandValue( // dividing a number to power of 2 -> shifting right
            Operand.COMBO,
            operand
        ).toInt() // by default rounds to zero
    jump()
}

fun jump() {
    ip = ip!! + 2
}

/**
 * Part 1 is pretty straightforward if you can manage to define the instructions
 * correctly
 */
fun solve1() {
    while (ip in pr!!.indices) {
        val ins = pr!![ip!!]
        val ope = pr!![ip!! + 1]
        when (ins) {
            0 -> adv(ope)
            1 -> bxl(ope)
            2 -> bst(ope)
            3 -> jnz(ope)
            4 -> bxc()
            5 -> out(ope)
            6 -> bdv(ope)
            7 -> cdv(ope)
            else -> throw RuntimeException("This shouldn't happen")
        }
    }
    println("1: ${ou.joinToString(",")}")
    ip = 0
    rb = 0
    rc = 0
    ou.clear()
}

/**
 * This function runs the program just one time and returns the number in the output
 */
fun runProgramOneTime(): Int {
    while (ip in pr!!.indices) {
        val ins = pr!![ip!!]
        val ope = pr!![ip!! + 1]
        when (ins) {
            0 -> adv(ope)
            1 -> bxl(ope)
            2 -> bst(ope)
            3 -> jnz(ope)
            4 -> bxc()
            5 -> out(ope)
            6 -> bdv(ope)
            7 -> cdv(ope)
            else -> throw RuntimeException("This shouldn't happen")
        }
    }
    val result = ou[0]
    ip = 0
    rb = 0
    rc = 0
    ou.clear()
    return result
}

/**
 * Part 2 was very fun to implement after realizing the pattern of the program.
 * Program outputs one number at each iteration. And at each iteration value at
 * register A shifts right 3 times(gets divided by 8). So we can conclude that
 * at the last iteration, program should output 0. Since everything starts with
 * register A, when we find 3 bits that makes the program result in 0, we end up
 * finding the FIRST 3 bits of expected register A value at the end. Running the
 * program for each value in the program code reverse, while updating the register
 * A value gives the target.
 */
fun solve2() {
    val targets = pr!!.reversed()
    var a = 0L
    var cur = 0 // current target index, that program should output
    var skip = 0 // !!crucial for recalculation when no matching found
    while (cur in targets.indices) {
        val target = targets[cur]
        var success = false
        for (i in 0 until 8) {
            ra = a + i
            val programResult = runProgramOneTime()
            if (target == programResult) {
                if (skip == 0) {
                    a += i
                    success = true
                    break
                } else {
                    skip--
                }
            }
        }
        if (success) {
            cur++
            a = a shl 3 // Shift left and append bits
        } else { // If no 3 bit found, we need to go back and find the next smallest alternative for the PREVIOUS 3 bits
            cur--
            skip++ // This will help skipping the previously used alternatives
            a = a shr 3
        }
    }
    println(a shr 3)
}


fun readInput() {
    val (registers, instructions) = Files.readString(Paths.get("input\\day17_input.txt"))
        .split("\r\n\r\n".toRegex(), limit = 2)

    fun gR(l: String): Long {
        return l.split(":", limit = 2)[1].trim().toLong()
    }

    registers.lines().forEachIndexed { i, l ->
        when (i) {
            0 -> ra = gR(l)
            1 -> rb = gR(l)
            2 -> rc = gR(l)
        }
    }
    pr = instructions.split(":", limit = 2)[1].split(",").map(String::trim)
        .map(String::toInt)
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
