import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

var input: String? = null
var memorySize: Int? = null
var memoryLayout: IntArray? = null
var freeSpaces: MutableList<Pair<Int, Int>> = mutableListOf()// (Index, Size)
var files: MutableList<Triple<Int, Int, Int>> =
    mutableListOf() // (Index, Size, ID)

/**
 * It was an easy puzzle, but it took me so long to come up with a better approach
 * for part 2.
 * For the first part, I represented memory with an Array and swap the values.
 * I used the same Array approach to represent the memory, but this time I stored
 * free spaces and files in different lists with their corresponding indices and sizes
 */
fun solve1() {
    var pointer1 = 0
    var pointer2 = memorySize!! - 1
    while (pointer1 < pointer2) {
        if (memoryLayout!![pointer1] == -1) {
            while (memoryLayout!![pointer2] == -1) {
                pointer2--
            }
            memoryLayout!![pointer1] = memoryLayout!![pointer2]
            memoryLayout!![pointer2] = -1
            pointer2--
        }
        pointer1++
    }
    var result1 = 0L
    for (i in memoryLayout!!.indices) {
        if (memoryLayout!![i] == -1) break
        result1 += i * memoryLayout!![i]
    }
    println("1: $result1")

}

fun solve2() {
    files.reversed().forEach { file ->
        var idx = 0
        while (freeSpaces[idx].second < file.second && freeSpaces[idx].first < file.first) {
            idx++
        }
        if (freeSpaces[idx].second >= file.second && freeSpaces[idx].first < file.first) {
            memoryLayout!!.fill(
                file.third,
                freeSpaces[idx].first,
                freeSpaces[idx].first + file.second
            )
            memoryLayout!!.fill(-1, file.first, file.first + file.second)

            if (freeSpaces[idx].second > file.second) {
                freeSpaces[idx] = Pair(
                    freeSpaces[idx].first + file.second,
                    freeSpaces[idx].second - file.second
                )
            } else {
                freeSpaces.removeAt(idx)
            }
        }
    }
    //println(memoryLayout.contentToString())
    var result = 0L
    for (i in memoryLayout!!.indices) {
        if (memoryLayout!![i] != -1) result += i * memoryLayout!![i]
    }
    println("2: $result")
}


fun readInput() {
    var currentMemoryUnit = MemoryUnit.FILE
    input = Files.readString(Paths.get("input\\day9_input.txt"))
    memorySize = input!!.map(Char::digitToInt).sum()
    memoryLayout = IntArray(memorySize!!) { -1 }
    var cursor = 0
    var id = 0
    input!!.forEach {
        val v = it.digitToInt()
        when (currentMemoryUnit) {
            MemoryUnit.FREE_SPACE -> {
                freeSpaces.add(Pair(cursor, v))
                cursor += v
            }

            MemoryUnit.FILE -> {
                val jump = it.digitToInt()
                memoryLayout!!.fill(id, cursor, cursor + jump)
                files.add(Triple(cursor, jump, id))
                cursor += jump
                id += 1
            }
        }
        currentMemoryUnit = currentMemoryUnit.alt()
    }
    //println(memoryLayoutAsList)
}

ManualClassLoader.load()
val p = measureTime {
    readInput()
}

val t1 = measureTime {
    solve1()
}

freeSpaces.clear()
files.clear()
readInput() // Just for refreshing the memory array

val t2 = measureTime {
    solve2()
}

println("P: $p")
println("T1: $t1")
println("T2: $t2")

/**
 * Utilities -----------------------------------------
 */
enum class MemoryUnit {
    FILE,
    FREE_SPACE
}

fun MemoryUnit.alt(): MemoryUnit = when (this) {
    MemoryUnit.FILE -> MemoryUnit.FREE_SPACE
    MemoryUnit.FREE_SPACE -> MemoryUnit.FILE
}

// ---------------------------------------------------


/**
 * Dummy class to warm-up JVM, before running the solution operations
 */
class Dummy {
    fun m() {
    }
}

/**
 * Utility to warm-up JVM
 */
object ManualClassLoader {
    internal fun load() {
        for (i in 0..99999) {
            val dummy = Dummy()
            dummy.m()
        }
    }
}
