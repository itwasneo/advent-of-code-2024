import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime

var buyers = listOf<Long>()

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

println("P: $p")
println("T1: $t1")