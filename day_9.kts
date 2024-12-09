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
    files.reversed().forEach { f ->
        var idx = 0
        val (fileStart, fileSize, fileId) = f
        while (freeSpaces[idx].second < fileSize && freeSpaces[idx].first < fileStart) {
            idx++
        }
        val (spaceStart, spaceSize) = freeSpaces[idx]
        if (spaceSize >= fileSize && spaceStart < fileStart) {
            memoryLayout!!.fill(
                fileId,
                spaceStart,
                spaceStart + fileSize
            )
            memoryLayout!!.fill(-1, fileStart, fileStart + fileSize)

            if (spaceSize > fileSize) {
                freeSpaces[idx] = Pair(
                    spaceStart + fileSize,
                    spaceSize - fileSize
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
    input = Files.readString(Paths.get("input\\day9_input.txt"))
    memorySize = input!!.map(Char::digitToInt).sum()
    memoryLayout = IntArray(memorySize!!) { -1 }
    var cursor = 0
    var id = 0
    var isFile = true
    input!!.forEach {
        val v = it.digitToInt()
        if (isFile) {
            val jump = it.digitToInt()
            memoryLayout!!.fill(id, cursor, cursor + jump)
            files.add(Triple(cursor, jump, id))
            cursor += jump
            id += 1
        } else {
            freeSpaces.add(Pair(cursor, v))
            cursor += v
        }
        isFile = !isFile
    }
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
