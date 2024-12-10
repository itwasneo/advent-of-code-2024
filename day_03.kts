import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

val (content, duration) = measureTimedValue {
    Files.readString(Path("input\\day03_input.txt"))
}

println("P: $duration")

fun solve1() {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val part1 = regex.findAll(content).map { matchResult ->
        matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
    }.sum()

    println("1: $part1")
}

fun solve2() {
    val r = """(do\(\))|(don't\(\))|(mul\((\d+),(\d+)\))""".toRegex()
    var switchOn = 1
    var result = 0
    r.findAll(content).forEach { m ->
        when {
            m.groupValues[1].isNotEmpty() -> switchOn = 1
            m.groupValues[2].isNotEmpty() -> switchOn = 0
            m.groupValues[3].isNotEmpty() -> result += m.groupValues[4].toInt() * m.groupValues[5].toInt() * switchOn
        }
    }
    println("2: $result")

}

val t = measureTime {
    solve1()
    solve2()
}
println("T: $t")