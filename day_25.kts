import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

val height = 7
val keys = mutableListOf<IntArray>()
val locks = mutableListOf<IntArray>()


fun readInput() {
    var counter = 0
    val key = IntArray(5) { 5 }
    val lock = IntArray(5) { 0 }
    var isLock = true
    for (line in Files.readAllLines(Paths.get("input/day25_input.txt"))) {
        if (counter == 0 && line.all { it == '.' }) {
            isLock = false
            counter++
            continue
        } else if (counter == 0 && line.all { it == '#' }) {
            isLock = true
            counter++
            continue
        } else if (line.isEmpty()) {
            if (isLock) {
                locks.add(lock.clone())
                lock.fill(0)
            } else {
                keys.add(key.clone())
                key.fill(5)
            }
            counter = 0
            continue
        }
        if (isLock) {
            line.forEachIndexed { idx, c ->
                if (c == '#') {
                    lock[idx] += 1
                }
            }
        } else {
            line.forEachIndexed { idx, c ->
                if (c == '.') {
                    key[idx] -= 1
                }
            }
        }
        counter++
    }
    if (isLock) {
        locks.add(lock.clone())
    } else {
        keys.add(key.clone())
    }
}

fun solve1() {
    var result = 0
    for (lock in locks) {
        key@ for (key in keys) {
            for (i in key.indices) {
                if (key[i] + lock[i] > 5) continue@key
            }
            result++
        }
    }

    println("1: $result")
}

val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

println("P: $p")
println("T1: $t1")