import java.nio.file.Files
import java.nio.ByteBuffer
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.time.measureTime

val arr1 = IntArray(1000)
val arr2 = IntArray(1000)

private fun readFileWithBuffer(fileName: String) {
	val path = Paths.get(fileName)
	val fileChannel = Files.newByteChannel(path)
	val bufferSize = 1400
	val buffer = ByteBuffer.allocate(bufferSize)
	var idx = 0

	while (fileChannel.read(buffer) != -1) {
		buffer.flip() // Prepare the buffer for reading

		// Process the buffer content in blocks
		do {
			val l = ByteArray(5)
			val r = ByteArray(5)
			buffer.get(l)
			buffer.position(buffer.position() + 3)
			buffer.get(r)
			buffer.position(buffer.position() + 1)

			val lv = String(l, Charsets.UTF_8).toInt()
			val rv = String(r, Charsets.UTF_8).toInt()

			// Assuming arr1 and arr2 are already defined and have the appropriate size
			arr1[idx] = lv
			arr2[idx] = rv
			idx += 1
		} while (buffer.remaining() >= 13)

		buffer.clear() // Clear the buffer for the next read
	}

	fileChannel.close() // Close the file channel}
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
	readFileWithBuffer("input\\day1_input.txt")
}

println("P: $fileReadDuration")

val duration = measureTime {
	solvePart1()
	solvePart2()
}
println("T: $duration")

