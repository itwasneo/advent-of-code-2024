package com.itwasneo

import java.io.File

class App {
    fun readInput(fileName: String) = File(fileName).forEachLine { println(it) }
}

fun main() {
    println(App().readInput("/home/iwn/git/advent-of-code-2024/app/src/main/resources/input.txt"))
}
