import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.time.measureTime

var r: HashMap<Int, MutableSet<Int>> = HashMap()
var u: MutableList<MutableList<Int>> = mutableListOf()
var u2: MutableList<MutableList<Int>> = mutableListOf() // The second part's input

fun readFile() {
    var s = true
    Files.readAllLines(Path("input\\day5_input.txt")).forEach { l ->
        if (l.isBlank()) {
            s = false
        } else if (s) {
            val (k, v) = l.split("|", limit = 2)
                .map(String::toInt)
            if (r[k].isNullOrEmpty()) {
                r[k] = mutableSetOf(v)
            } else {
                r[k]?.add(v)
            }
        } else {
            u.add(
                l.split(",").map(String::toInt)
                    .toMutableList()
            )
        }
    }
}

fun solve1() {
    val result = u.sumOf { update ->
        val seen = mutableSetOf<Int>()
        var valid = true
        for (cu in update) {
            if (!r[cu].isNullOrEmpty() && r[cu]!!.any { seen.contains(it) }) {
                valid = false
                u2.add(update) // This prepares the input for the second part :)
                break
            }
            seen.add(cu)
        }
        if (valid) update[update.size / 2] else 0
    }
    println("1: $result")
}

data class Node(var v: Int, var next: Node?)

data class LinkedList(var head: Node?)

fun solve2() {
    val result = u2.sumOf { update ->
        val ll = LinkedList(null)
        for (n in update) {
            placeNode(n, ll)
        }
        getMid(ll.head, update.size/2)
    }

    println("2: $result")
}

fun placeNode(c: Int, ll: LinkedList) {
    if (ll.head == null) {
        ll.head = Node(c, null)
    } else {
        if (r[c] != null && r[c]!!.contains(ll.head!!.v)) {
            val newHead = Node(c, ll.head)
            ll.head = newHead
        } else {
            var cur = ll.head
            while (cur != null) {
                if (cur.next == null) {
                    cur.next = Node(c, null)
                    break
                } else if (r[c] != null && r[c]!!.contains(cur.next!!.v)) {
                    cur.next = Node(c, cur.next)
                    break
                } else {
                    cur = cur.next
                }
            }
        }
    }
}

fun printLinkedList(node: Node?) {
    if (node != null) {
        print("${node.v} --> ")
        printLinkedList(node.next)
    }
}

fun getMid(node: Node?, mid: Int): Int {
    var cur = node
    repeat(mid) {
        cur = cur!!.next
    }
    return cur!!.v
}

val p = measureTime {
    readFile()
}

val t = measureTime {
    solve1()
    solve2()
}

println("P: $p")
println("T: $t")