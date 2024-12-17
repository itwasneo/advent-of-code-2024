import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

// Registers
var ra: Int? = null
var rb: Int? = null
var rc: Int? = null

// Instruction Pointer
var ip: Int? = 0 // Increases by 2 after each instructions (opcode + operand)

// Output
var ou: String = ""

// Program
var pr: List<Int>? = null

enum class Operand {
    LITERAL,
    COMBO
}

fun getOperandValue(operand: Operand, id: Int): Int {
    return when (operand) {
        Operand.LITERAL -> id
        Operand.COMBO -> when (id) {
            0, 1, 2, 3 -> id
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
    ra = ra!! / (1 shl getOperandValue(
        Operand.COMBO,
        operand
    )) // by default rounds to zero
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
    rb = getOperandValue(Operand.COMBO, operand) % 8
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
    if (ra!! != 0) ip =
        getOperandValue(Operand.LITERAL, operand) else jump()
}

/**
 * bxc (bitwise XOR) Write the result to register B. Operand is not used!!
 * opcode: 4
 * left: rb
 * right: rc
 */
fun bxc(operand: Int) {
    rb = rb!! xor rc!!
    jump()
}

/**
 * out (print) Outputs the operand COMBO modulo 8.
 * opcode: 5
 * input: operand(COMBO)
 */
fun out(operand: Int) {
    ou += "${getOperandValue(Operand.COMBO, operand) % 8},"
    jump()
}

/**
 *  bdv (division) truncates (rounding to zero) the result to an integer. Writes
 *  the result into register B
 *  opcode: 6
 *  numerator:      ra
 *  denominator:    operand(COMBO)
 */
fun bdv(operand: Int) {
    rb = ra!! / (1 shl getOperandValue(
        Operand.COMBO,
        operand
    )) // by default rounds to zero
    jump()
}

/**
 *  bdv (division) truncates (rounding to zero) the result to an integer. Writes
 *  the result into register C
 *  opcode: 7
 *  numerator:      ra
 *  denominator:    operand(COMBO)
 */
fun cdv(operand: Int) {
    rc = ra!! / (1 shl getOperandValue(
        Operand.COMBO,
        operand
    )) // by default rounds to zero
    jump()
}

fun jump() {
    ip = ip!! + 2
}

fun solve1() {
    while (ip in pr!!.indices) {
        val ins = pr!![ip!!]
        val ope = pr!![ip!! + 1]
        when (ins) {
            0 -> adv(ope)
            1 -> bxl(ope)
            2 -> bst(ope)
            3 -> jnz(ope)
            4 -> bxc(ope)
            5 -> out(ope)
            6 -> bdv(ope)
            7 -> cdv(ope)
            else -> throw RuntimeException("This shouldn't happen")
        }
    }
    println(ou)
}


fun readInput() {
    val (registers, instructions) = Files.readString(Paths.get("input\\day17_input_example.txt"))
        .split("\r\n\r\n".toRegex(), limit = 2)

    fun gR(l: String): Int {
        return l.split(":", limit = 2)[1].trim().toInt()
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

    /*
    println("ra: $ra")
    println("rb: $rb")
    println("rc: $rc")
    println("Instructions: $pr")
     */
}

val p = measureTime {
    readInput()
}
println("P: $p")

val t1 = measureTime {
    solve1()
}
println("T1: $t1")