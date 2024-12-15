package year2024.day02

import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {
    val year = 2024
    val day = 2

    fun isSafe(steps: List<Int>): Boolean {
        val sameSign = steps.all { it < 0 } || steps.all { it > 0 }
        return sameSign && steps.all { abs(it) in 1..3 }
    }

    fun part1(input: List<String>): Int {
        val count = input.count { line ->
            val report = line.split(" ")
            val steps = report.zipWithNext().map { (l, r) -> l.toInt() - r.toInt() }
            isSafe(steps)
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val count = input.count { line ->
            val report = line.split(" ")
            val steps = report.zipWithNext().map { (l, r) -> l.toInt() - r.toInt() }
            var safe = isSafe(steps)
            if (!safe) {
                for (i in report.indices) {
                    val permutation = report.toMutableList().also { it.removeAt(i) }
                        .zipWithNext().map { (l, r) -> l.toInt() - r.toInt() }
                    safe = isSafe(permutation)
                    if (safe)
                        return@count safe
                }
            }
            safe
        }
        return count
    }


    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 2)
    // Read the input from the `src/Day01.txt` file.
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 4)
    part2(input).println()
}
