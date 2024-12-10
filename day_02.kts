import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

val (input, readDuration) = measureTimedValue {
    Files.readAllLines(Paths.get("input\\day02_input.txt"))
        .map { line ->
            line.split(" ").map(String::toInt)
        }
}

println("P: $readDuration")

fun solve() {
    var resultPart1 = 0
    var resultPart2 = 0
    val duration = measureTime {
        input.forEach {
            resultPart1 += isSafe(it)
            resultPart2 += isSafeWithToleration(it)
        }
    }
    println("T: $duration")
    println("1: $resultPart1")
    println("2: $resultPart2")
}

/**
 * Validation function for Part 1. Returns 1 if report is valid, 0 invalid
 */
fun isSafe(report: List<Int>): Int {
    val rt = getReportType(report[0], report[1])
    return if (report.zipWithNext()
            .all { (a, b) -> isValid(a, b, rt) }
    ) 1 else 0
}

/**
 * Return the problematic index
 */
fun isReportSafe(report: List<Int>): Int {
    val rt = getReportType(report[0], report[1])
    return report.zipWithNext()
        .indexOfFirst { (a, b) -> !isValid(a, b, rt) }
        .takeIf { it >= 0 } ?: (report.size - 1)
}

/**
 * Once the validation fail, filter out the i, i-1, i+1 indeces
 * one by one and revalidate again
 */
fun isSafeWithToleration(report: List<Int>): Int {
    val i = isReportSafe(report)
    if (i == report.size - 1) return 1
    val fr = report.filterIndexed { index, _ -> index != i }
    if (isReportSafe(fr) == fr.size - 1) return 1
    val flr = report.filterIndexed { index, _ -> index != i - 1 }
    if (isReportSafe(flr) == flr.size - 1) return 1
    val frr = report.filterIndexed { index, _ -> index != i + 1 }
    if (isReportSafe(frr) == frr.size - 1) return 1
    return 0
}

// Utility functions for readibility -------------------------------------------------
enum class ReportType {
    INC,
    DEC,
    ERR
}

fun getReportType(l: Int, r: Int): ReportType = when {
    r > l -> ReportType.INC
    l > r -> ReportType.DEC
    else -> ReportType.ERR
}

fun checkOrder(l: Int, r: Int, reportType: ReportType): Boolean =
    when (reportType) {
        ReportType.INC -> r > l
        ReportType.DEC -> l > r
        ReportType.ERR -> false
    }

fun checkDiff(l: Int, r: Int, reportType: ReportType): Boolean {
    val diff = when (reportType) {
        ReportType.INC -> r - l
        ReportType.DEC -> l - r
        ReportType.ERR -> -1
    }
    return diff in 1..3
}

fun isValid(l: Int, r: Int, reportType: ReportType): Boolean {
    return checkOrder(l, r, reportType) && checkDiff(l, r, reportType)
}
// -----------------------------------------------------------------------------

solve()
