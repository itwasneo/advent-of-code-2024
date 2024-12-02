import java.io.File
import kotlin.math.abs

fun solvePart1(fileName: String) {
	var result = 0
	File(fileName).forEachLine {
		val a = it.split(" ").map(String::toInt)
		result += isSafe(a)
	}
	println("Part 1: $result")
}

fun solvePart2(fileName: String) {
	var result = 0
	File(fileName).forEachLine {
		val a = it.split(" ").map(String::toInt)
		result += isSafeWithToleration(a)
	}
	println("Part 2: $result")
}

fun isSafe(report: List<Int>): Int {
	val reportType = getReportType(report[0], report[1])

	var index = 0

	while (index < report.size - 1) {
		if (!checkOrder(report[index], report[index+1], reportType) || !checkDiff(report[index], report[index+1], reportType)) {
			return 0
		}
		index += 1
	}
	return 1
}

fun isReportSafe(report: List<Int>): Int {

	val reportType = getReportType(report[0], report[1])

	var index = 0

	while (index < report.size - 1) {
		if (!checkOrder(report[index], report[index+1], reportType) || !checkDiff(report[index], report[index+1], reportType)) {
			return index
		}
		index += 1
	}
	return index
}

fun isSafeWithToleration(report: List<Int>): Int {
	val i = isReportSafe(report)
	if (i == report.size - 1) return 1
	val fr = report.filterIndexed { index, _ -> index != i }
	if (isReportSafe(fr) == fr.size - 1) return 1
	val flr = report.filterIndexed { index, _ -> index != i - 1 }
	if (isReportSafe(flr) == flr.size - 1) return 1
	val frr = report.filterIndexed { index, _ -> index != i + 1 }
	if (isReportSafe(frr) == frr.size - 1) return 1
	else return 0
}



enum class ReportType {
	INC,
	DEC,
	ERR
}

fun getReportType(l: Int, r: Int): ReportType {
	if (r > l) return ReportType.INC
	else if (l > r) return ReportType.DEC
	else return ReportType.ERR
}

fun checkOrder(l: Int, r: Int, reportType: ReportType): Boolean {
	return when (reportType) {
		ReportType.INC -> r > l
		ReportType.DEC -> l > r
		ReportType.ERR -> false
	}
}

fun checkDiff(l: Int, r: Int, reportType: ReportType): Boolean {
	val diff = when (reportType) {
		ReportType.INC -> r - l
		ReportType.DEC -> l - r
		ReportType.ERR -> -1
	}
	return diff >= 1 && diff <= 3
}

fun isValid(l: Int, r: Int, reportType: ReportType): Boolean {
	return checkOrder(l, r, reportType) && checkDiff(l, r, reportType) 
}

fun isSafeModified(report: List<Int>): Int {
	val reportType = getReportType(report[0], report[1])

	return 0
}

solvePart1("/home/iwn/git/advent-of-code-2024/input/day2_input.txt")
solvePart2("/home/iwn/git/advent-of-code-2024/input/day2_input.txt")
