import java.io.File
import kotlin.math.abs

private fun solvePart1() {

	val (list1, list2) = File("/home/iwn/git/advent-of-code-2024/app/src/main/resources/day1_input.txt")
		.readLines()
		.map {
			val (a, b) = it.trim().split("   ").map(String::toInt)
			a to b
		}
		.unzip()

	val result = list1.sorted().zip(list2.sorted()) { a, b -> abs(a - b) }.sum()
	println("Part 1: $result")
}

private fun solvePart2() {

	val (list1, list2) = File("/home/iwn/git/advent-of-code-2024/app/src/main/resources/day1_input.txt")
		.readLines()
		.map {
			val (a, b) = it.trim().split("   ").map(String::toInt)
			a to b
		}
		.unzip()

	val frequencyMap = list2.groupingBy { it }.eachCount()
	val result = list1.sumOf {
		it * (frequencyMap[it] ?: 0)
	}
	println("Part 2: $result")
}

fun main() {
	solvePart1()
	solvePart2()
}
