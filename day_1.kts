import java.io.File
import kotlin.math.abs
import kotlin.time.measureTimedValue

private fun readFileWithReadLines(fileName: String): Pair<List<Int>, List<Int>> {
	return File(fileName)
		.readLines()
		.map {
			val (a, b) = it.trim().split("   ").map(String::toInt)
			a to b
		}
		.unzip()
}

private fun solvePart1() {
	val result = list1.sorted().zip(list2.sorted()) { a, b -> abs(a - b) }.sum()
	println("Part 1: $result")
}

private fun solvePart2() {
	val frequencyMap = list2.groupingBy { it }.eachCount()
	val result = list1.sumOf {
		it * (frequencyMap[it] ?: 0)
	}
	println("Part 2: $result")
}

val timedResult = measureTimedValue {
		readFileWithReadLines("/home/iwn/git/advent-of-code-2024/input/day1_input.txt")
	}
val (list1, list2) = timedResult.value

println("File Read Elapsed Time: ${timedResult.duration}")

solvePart1()

solvePart2()
