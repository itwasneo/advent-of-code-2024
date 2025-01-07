import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

var buyers = listOf<Long>()
val sharedSequenceCountMaps = mutableMapOf<List<Int>, Int>()

fun readInput() {
    buyers = Files.readAllLines(Paths.get("input/day22_input.txt"))
        .map { it.toLong() }
}

fun solve1() {
    val result = buyers.sumOf {
        var secret = it
        repeat(2000) {
            secret = evolveSecret(secret)
        }
        secret
    }
    println("1: $result")
}

fun solve2() {
    buyers.forEach { s ->
        val diffSequence = arrayOfNulls<Pair<Int, Int>>(2000)
        val seen = mutableSetOf<List<Int>>()
        var secret = s
        var currentPrice = getPrice(secret)
        var idx = 0
        while (idx < 2000) {
            val newSecret = evolveSecret(secret)
            val nextPrice = getPrice(newSecret)
            diffSequence[idx] =
                Pair((nextPrice - currentPrice).toInt(), nextPrice.toInt())
            secret = newSecret
            currentPrice = nextPrice
            idx++
        }
        diffSequence.toList()
            .windowed(size = 4, step = 1, partialWindows = false)
            .forEach { seq ->
                val currentSequence = seq.map { it!!.first }
                val price = seq.last()!!.second
                if (!seen.contains(currentSequence)) {
                    sharedSequenceCountMaps[currentSequence] =
                        sharedSequenceCountMaps.getOrDefault(
                            currentSequence,
                            0
                        ) + price
                }
                seen.add(currentSequence)
            }
    }

    println("2: ${sharedSequenceCountMaps.values.maxOrNull()}")
}

private fun getPrice(secret: Long): Long {
    return secret % 10
}

private fun mixValueIntoSecret(value: Long, secret: Long): Long {
    return value xor secret
}

private fun pruneSecretNumber(number: Long): Long {
    val mask = 16777215L // 2^24 - 1
    return number and mask
}

private fun multiplyBy64(number: Long): Long {
    return number shl 6
}

private fun divideBy32(number: Long): Long {
    return number shr 5
}

private fun multiplyBy2048(number: Long): Long {
    return number shl 11
}

private fun evolveSecret(secret: Long): Long {
    val step1 =
        pruneSecretNumber(mixValueIntoSecret(multiplyBy64(secret), secret))
    val step2 = pruneSecretNumber(mixValueIntoSecret(divideBy32(step1), step1))
    val step3 =
        pruneSecretNumber(mixValueIntoSecret(multiplyBy2048(step2), step2))
    return step3
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