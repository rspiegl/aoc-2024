package year2024.day01

import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {
    val year = 2024
    val day = 1

    fun part1(input: List<String>): Int {
        val (left, right) = input.map { it.split("   ").let { it[0] to it[1] } }.unzip()
        val distance = left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l.toInt() - r.toInt()) }
        return distance
    }

    fun part2(input: List<String>): Int {
        val (left, right) = input.map { it.split("   ").let { it[0] to it[1] } }.unzip()
        val rightCounts = right.groupingBy { it }.eachCount()
        val similarityScore = left.sumOf { it.toInt() * (rightCounts[it] ?: 0) }
        return similarityScore
    }

    // Or read a large test input from the `src/Day01/Day01_test.txt` file:
    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01/Day01.txt` file.
    val input = readInput(year, day)
    part1(input).println()
    part2(input).println()
}
