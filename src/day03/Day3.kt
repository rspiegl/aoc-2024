package day03

import utils.println
import utils.readInput

fun main() {
    val latestDay = 3

    fun mulCommand(match: String): Int {
        val operators = "\\d{1,3}".toRegex().findAll(match)
        return operators.map { it.groupValues[0].toInt() }.reduce { acc, i -> acc * i }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val regex = "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex()
            val matches = regex.findAll(line).map { it.groupValues[0] }.toList()
            val sum = matches.sumOf(::mulCommand)
            sum
        }
    }

    fun part2(input: List<String>): Int {
        val line = input.reduce(String::plus)
        val regex = "(mul\\(\\d{1,3},\\d{1,3}\\))|(do\\(\\))|(don't\\(\\))".toRegex()
        val matches = regex.findAll(line).map { it.groupValues[0] }.toList()
        var enabled = true
        return matches.sumOf { match ->
            var sum = 0
            if (match == "do()") {
                enabled = true
            }
            else if (match == "don't()") {
                enabled = false
            }
            else if (enabled) {
                sum = mulCommand(match)
            }
            sum
        }
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 161)
    val input = readInput("$latestDay")
    part1(input).println()

    val testInput2 = readInput("$latestDay", suffix = "_test_2")
    check(part2(testInput2) == 54)
    part2(input).println()
}
