import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

var input: String? = null
var memorySize: Int? = null
var memoryLayout: IntArray? = null
var memoryLayoutAsList: MutableList<MemoryBlock> = mutableListOf()

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

fun solve2() {
    for (v in memoryLayoutAsList.reversed()) {
        if (v.occupiedType == MemoryUnit.FILE) {
            var p = 0
            var cmb = memoryLayoutAsList[p]
            while (cmb.occupiedType == MemoryUnit.FREE_SPACE && cmb.capacity < v.size || cmb.occupiedType == MemoryUnit.FILE && cmb.id != v.id) {
                p++
                cmb = memoryLayoutAsList[p]
            }
            if (cmb.id != v.id) {
                cmb.id = v.id
                cmb.size = v.size
                if (cmb.capacity > v.size) {
                    val newSize = cmb.capacity - cmb.size
                    memoryLayoutAsList.add(
                        p + 1,
                        MemoryBlock(
                            null,
                            newSize,
                            newSize,
                            MemoryUnit.FREE_SPACE
                        )
                    )
                }
                cmb.capacity = v.size
                cmb.occupiedType = MemoryUnit.FILE
                v.occupiedType = MemoryUnit.FREE_SPACE
                v.id = null
            }

        }
    }
    var result = 0L
    var idx = 0
    memoryLayoutAsList.forEach {
        when (it.occupiedType) {
            MemoryUnit.FILE -> {
                val r = it.size
                val p = it.id
                val diff = it.capacity - it.size
                repeat(r) {
                    result += idx * p!!
                    idx++
                }
                repeat(diff) {
                    idx++
                }
            }

            MemoryUnit.FREE_SPACE -> {
                repeat(it.capacity) {
                    idx++
                }
            }
        }
    }
    println("2: $result")
}

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

ManualClassLoader.load()
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
