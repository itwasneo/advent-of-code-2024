import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

var input: String? = null
var memorySize: Int? = null
var memoryLayout: IntArray? = null
var memoryLayoutAsList: MutableList<MemoryBlock> = mutableListOf()

/**
 * It was an easy puzzle, but it took me so long to come up with a better approach
 * for part 2.
 * For the first part, I represented memory with an Array and swap the values.
 * For the second part, I created a custom class to represent MemoryBlock and put
 * them into a list. This prevents me to make any pointer arithmetic while swapping
 * the memory blocks, on the other hand, it has a very inefficient insert operation.
 * Since you can put files with sizes smaller than the free space, it creates new
 * free spaces that you need to add to the list :(
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
    for (block in memoryLayoutAsList.reversed()) {
        if (block.occupiedType == MemoryUnit.FILE) {
            var index = 0
            var cmb = memoryLayoutAsList[index]
            while (cmb.occupiedType == MemoryUnit.FREE_SPACE && cmb.capacity < block.size || cmb.occupiedType == MemoryUnit.FILE && cmb.id != block.id) {
                index++
                cmb = memoryLayoutAsList[index]
            }
            if (cmb.id != block.id) {
                cmb.id = block.id
                cmb.size = block.size
                if (cmb.capacity > block.size) {
                    val remainingCapacity = cmb.capacity - cmb.size
                    memoryLayoutAsList.add(
                        index + 1,
                        MemoryBlock(
                            null,
                            remainingCapacity,
                            remainingCapacity,
                            MemoryUnit.FREE_SPACE
                        )
                    )
                }
                cmb.capacity = block.size
                cmb.occupiedType = MemoryUnit.FILE
                block.occupiedType = MemoryUnit.FREE_SPACE
                block.id = null
            }

        }
    }
    var result = 0L
    var currentIndex = 0
    memoryLayoutAsList.forEach { block ->
        when (block.occupiedType) {
            MemoryUnit.FILE -> {
                repeat(block.size) {
                    result += currentIndex * block.id!!
                    currentIndex++
                }
                currentIndex += block.capacity - block.size
            }

            MemoryUnit.FREE_SPACE -> {
                currentIndex += block.capacity
            }
        }
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
                cursor += v
                memoryLayoutAsList.add(
                    MemoryBlock(
                        null,
                        v,
                        v,
                        MemoryUnit.FREE_SPACE
                    )
                )
            }

            MemoryUnit.FILE -> {
                val jump = it.digitToInt()
                memoryLayoutAsList.add(
                    MemoryBlock(
                        id,
                        v,
                        v,
                        MemoryUnit.FILE
                    )
                )
                for (i in cursor..<cursor + jump) {
                    memoryLayout!![i] = id
                }
                cursor += jump
                id += 1
            }
        }
        currentMemoryUnit = currentMemoryUnit.alt()
    }
    //println(memoryLayoutAsList)
}

//ManualClassLoader.load()
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

data class MemoryBlock(
    var id: Int?,
    var size: Int,
    var capacity: Int,
    var occupiedType: MemoryUnit
)
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
