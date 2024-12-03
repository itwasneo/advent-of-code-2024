import java.io.File
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.time.measureTime

val arr1 = IntArray(1000)
val arr2 = IntArray(1000)

private fun readFileWithBuffer(fileName: String) {
	var idx = 0
	File(fileName).forEachBlock(1400) { buffer, _ ->
		val byteBuffer = ByteBuffer.wrap(buffer)
		do {
			val l = ByteArray(5)
			val r = ByteArray(5)
			byteBuffer.get(l)
			byteBuffer.position(byteBuffer.position() + 3)
			byteBuffer.get(r)
			byteBuffer.position(byteBuffer.position() + 1)
			val lv = String(l, Charsets.UTF_8).toInt()
			val rv = String(r, Charsets.UTF_8).toInt()
			arr1[idx] = lv
			arr2[idx] = rv
			idx += 1
		} while(byteBuffer.remaining() >= 13)
	}
}

private fun solvePart1() {
	val result = arr1.sorted().zip(arr2.sorted()) { a, b -> abs(a - b) }.sum()
	println("1: $result")
}

private fun solvePart2() {
	val frequencyMap = arr2.toList().groupingBy { it }.eachCount()
	val result = arr1.sumOf {
		it * (frequencyMap[it] ?: 0)
	}
	println("2: $result")
}

val fileReadDuration = measureTime {
	readFileWithBuffer("/home/iwn/git/advent-of-code-2024/input/day1_input.txt")
}

println("P: $fileReadDuration")

val duration = measureTime {
	solvePart1()
	solvePart2()
}
println("T: $duration")

