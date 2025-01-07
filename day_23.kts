import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val connectionMap = mutableMapOf<String, MutableSet<String>>()
val trinities = mutableSetOf<Set<String>>()

fun readInput() {
    Files.readAllLines(Paths.get("input/day23_input.txt"))
        .forEach { line ->
            val (p1, p2) = line.split("-", limit = 2)
            connectionMap.getOrPut(p1) { mutableSetOf() }.add(p2)
            connectionMap.getOrPut(p2) { mutableSetOf() }.add(p1)
        }
}

fun solve1() {
    connectionMap.forEach { (k, v) ->
        v.forEach { second ->
            val other = connectionMap[second] ?: error("No connection")
            v.intersect(other).forEach { third ->
                trinities.add(setOf(k, second, third))
            }
        }
    }

    val result = trinities.count { it.any { pc -> pc.startsWith("t") } }

    println("1: $result")
}

fun solve2() {
    fun isClique(nodes: List<String>): Boolean {
        var last = nodes.last()
        for (i in 0 until nodes.size - 1) {
            if (!connectionMap[nodes[i]]!!.contains(last) && nodes[i] != last) return false
        }
        return true
    }

    tailrec fun findCliques(
        currentClique: List<String>,
        candidates: Set<String>,
        largestClique: List<String>
    ): List<String> {
        if (candidates.isEmpty()) {
            return if (currentClique.size > largestClique.size) currentClique else largestClique
        }

        val candidate = candidates.first()
        val newClique = currentClique + candidate
        return if (isClique(newClique)) {
            if (newClique.size > largestClique.size) {
                findCliques(newClique, candidates - candidate, newClique)
            } else {
                findCliques(newClique, candidates - candidate, largestClique)
            }
        } else {
            findCliques(
                currentClique,
                candidates - candidate,
                largestClique
            )
        }
    }

    val largestClique = connectionMap.keys.map {
        val connectionSet = connectionMap[it] ?: error("No connection")
        findCliques(listOf(it), connectionSet, emptyList())
    }.maxByOrNull { it.size } ?: error("No clique found")

    println("2: ${largestClique.sorted().joinToString(",")}")
}

val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

val t2 = measureTime {
    solve2()
}

println("P: $p")
println("T1: $t1")
println("T2: $t2")