package year2024.day07

import utils.permutationsWithReplacement
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 7

    fun part1(input: List<String>): Long {
        val operators = listOf<(Long, Long) -> Long>(Long::plus, Long::times)
        return input.sumOf { equation ->
            val testValue = equation.takeWhile { it != ':' }.toLong()
            val numbers = equation.split(":")[1].trim().split(" ").map { it.toLong() }
            val permutations = operators.permutationsWithReplacement(numbers.size - 1)
            permutations.forEach { permutation ->
                val sum = numbers.drop(1).foldIndexed(numbers[0]) { index, acc, number ->
                    val operator = permutation[index]
                    operator(acc, number)
                }
                if (sum == testValue) {
                    return@sumOf testValue
                }
            }
            0
        }
    }

    fun concatenate(a: Long, b: Long): Long {
        return (a.toString() + b.toString()).toLong()
    }

    fun part2(input: List<String>): Long {
        val operators = listOf<(Long, Long) -> Long>(Long::plus, Long::times, ::concatenate)
        return input.sumOf { equation ->
            val testValue = equation.takeWhile { it != ':' }.toLong()
            val numbers = equation.split(":")[1].trim().split(" ").map { it.toLong() }
            val permutations = operators.permutationsWithReplacement(numbers.size - 1)
            permutations.forEach { permutation ->
                val sum = numbers.drop(1).foldIndexed(numbers[0]) { index, acc, number ->
                    val operator = permutation[index]
                    operator(acc, number)
                }
                if (sum == testValue) {
                    return@sumOf testValue
                }
            }
            0
        }
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 3749L)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 11387L)
    part2(input).println()
}
