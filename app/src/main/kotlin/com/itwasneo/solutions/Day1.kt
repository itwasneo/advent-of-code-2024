package com.itwasneo.solutions

import java.io.File
import kotlin.math.abs

class Day1(override val inputFileName: String) : DailyPuzzle {

    override fun solve() {
        solvePart1()
        solvePart2()
    }

    private fun solvePart1() {
        val (list1, list2) = File(inputFileName)
            .readLines()
            .map {
                val (a, b) = it.trim().split("\\s+".toRegex()).map(String::toInt)
                a to b
            }
            .unzip()

        val result = list1.sorted().zip(list2.sorted()) { a, b -> abs( a - b) }.sum()

        println("Part 1: $result")
    }

    private fun solvePart2() {
        val (list1, list2) = File(inputFileName)
            .readLines()
            .map {
                val (a, b) = it.trim().split("\\s+".toRegex()).map(String::toInt)
                a to b
            }
            .unzip()

        val frequencyMap = list2.groupingBy { it }.eachCount()
        val result =list1.sumOf {
            it * (frequencyMap[it] ?: 0)
        }
        println("Part 2: $result")
    }
}