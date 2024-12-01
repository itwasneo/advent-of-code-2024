package com.itwasneo

import com.itwasneo.solutions.Day1
import java.io.File

class App {
    fun readInput(fileName: String) = File(fileName).forEachLine { println(it) }
}

const val inputFilePrefix: String = "/home/iwn/git/advent-of-code-2024/app/src/main/resources/"

fun main(args: Array<String>) {
    runCatching {
        val arg = args.toList()[0]
        val dayNo = arg.toInt()
        when (dayNo) {
            1 -> Day1(inputFilePrefix + "day1_input.txt").solve()
            else -> throw NotImplementedError()
        }
    }
}
