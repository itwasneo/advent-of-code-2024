import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.measureTime

/**
 * Relatively easy puzzle, just implemented a recursion with accumulators
 */
fun readAndSolve() {
    var part1 = 0L
    var part2 = 0L
    Files.newInputStream(Paths.get("input\\day7_input.txt")).use {
        val br = BufferedReader(InputStreamReader(it))
        var line: String? = br.readLine()
        while (!line.isNullOrEmpty()) {
            val (res, nums) = line.split(":", limit = 2).map(String::trim)
            val target = res.toLong()
            val ll = LinkedList(null)
            nums.split(" ").map(String::trim).map(String::toLong)
                .forEach { lvalue ->
                    ll.add(lvalue)
                }

            if (addMul(
                    ll.head!!.value,
                    ll.head!!.next,
                    target
                )
            ) {
                part1 += target
                part2 += target // Not sure whether they design the input that way but this may prevent to run modified recursion
            } // Part1 Calculation
            else if (addMulConcat(
                    ll.head!!.value,
                    ll.head!!.next,
                    target
                )
            ) part2 += target // Part2 Calculation
            line = br.readLine()
        }
    }
    println("1: $part1")
    println("2: $part2")
}

fun addMul(acc: Long, node: Node?, t: Long): Boolean {
    return if (acc > t) { // Very Nice optimization (Saw it on Reddit)
        false
    } else if (node != null) {
        addMul(acc + node.value, node.next, t) ||
                addMul(acc * node.value, node.next, t)
    } else {
        acc == t
    }
}

fun addMulConcat(acc: Long, node: Node?, t: Long): Boolean {
    return if (acc > t) { // Very Nice optimization (Saw it on Reddit)
        false
    } else if (node != null) {
        addMulConcat(acc + node.value, node.next, t) ||
                addMulConcat(acc * node.value, node.next, t) ||
                addMulConcat(
                    acc * 10.0.pow(
                        log10(node.value.toDouble()).toInt() + 1.0
                    ).toLong() + node.value,
                    node.next,
                    t
                )
    } else {
        acc == t
    }
}

data class Node(val value: Long, var next: Node?)

data class LinkedList(var head: Node?) {
    fun add(value: Long) {
        if (head == null) {
            head = Node(value, null)
        } else {
            var curr = head!!
            while (curr.next != null) {
                curr = curr.next!!
            }
            curr.next = Node(value, null)
        }
    }
}

val t = measureTime {
    readAndSolve()
}
println("T: $t")
