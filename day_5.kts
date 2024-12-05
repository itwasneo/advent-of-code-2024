import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.time.measureTime

var r: HashMap<String, MutableSet<String>> = HashMap()
var u: MutableList<MutableList<String>> = mutableListOf()
var u2: MutableList<MutableList<String>> =
    mutableListOf() // The second part's input

/**
 * Used a "seen" set.
 * Whenever a "seen" value, appeared in a rule set, that update line become invalid
 */
fun solve1() {
    val result = u.sumOf { update ->
        val seen = mutableSetOf<String>()
        var valid = true
        for (cu in update) {
            if (!r[cu].isNullOrEmpty() && r[cu]!!.any { seen.contains(it) }) {
                valid = false
                u2.add(update) // This prepares the input for the second part :)
                break
            }
            seen.add(cu)
        }
        if (valid) update[update.size / 2].toInt() else 0
    }
    println("1: $result")
}

data class Node(var v: String, var next: Node?)

data class LinkedList(var head: Node?)

/**
 * Used a linked list to sort the updates.
 */
fun solve2() {
    val result = u2.sumOf { update ->
        val ll = LinkedList(null)
        for (n in update) {
            placeNode(n, ll)
        }
        getAt(ll.head, update.size / 2)
    }

    println("2: $result")
}


/**
 * Given a linked list and a value, it finds the right spot to insert the new node
 * (according to the rule set)
 */
fun placeNode(c: String, ll: LinkedList) {
    val rules = r[c] ?: emptySet<Int>()
    if (ll.head == null || rules.contains(ll.head!!.v)) { // Insert head if null, or has priority
        ll.head = Node(c, ll.head)
        return
    }
    var cur = ll.head
    while (cur != null) {
        when {
            cur.next == null -> {
                cur.next = Node(c, null)
                return
            }

            rules.contains(cur.next!!.v) -> {
                cur.next = Node(c, cur.next)
                return
            }

            else -> cur = cur.next
        }
    }
}

/**
 * Debugging utility for printing nodes in a linked list
 */
fun printLinkedList(node: Node?) {
    if (node != null) {
        if (node.next == null) print("${node.v} --> ")
        else print("${node.v} --> ")
        printLinkedList(node.next)
    }
}

/**
 * Get the specific node value at an index
 */
fun getAt(node: Node?, at: Int): Int {
    var cur = node
    repeat(at) {
        cur = cur!!.next
    }
    return cur!!.v.toInt()
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

/**
 * Yes I parsed like this
 */
fun readFile() {
    var s = true
    Files.readAllLines(Path("input\\day5_input.txt")).forEach { l ->
        if (l.isBlank()) {
            s = false
        } else if (s) {
            val (k, v) = l.split("|", limit = 2)
            if (r[k].isNullOrEmpty()) {
                r[k] = mutableSetOf(v)
            } else {
                r[k]?.add(v)
            }
        } else {
            u.add(
                l.split(",").toMutableList()
            )
        }
    }
}

