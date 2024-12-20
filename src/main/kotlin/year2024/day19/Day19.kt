package year2024.day19

import arrow.core.MemoizedDeepRecursiveFunction
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 19

    fun part1(input: List<String>): Int {
        val towels = input[0].split(", ").groupBy { it[0] }
        val designs = input.drop(2)
        fun getTowelsInDesignSortedByLength(design: String): Set<String> {
            if (design.isEmpty()) return emptySet()
            val possibilities = towels[design[0]]?.sortedBy { it.length }?.reversed()
            return possibilities?.filter { design.startsWith(it) }?.toSortedSet() ?: emptySet()
        }

        val count = designs.count { design ->
            var arrangments = getTowelsInDesignSortedByLength(design)
            while (arrangments.isNotEmpty()) {
                arrangments = arrangments.flatMap { arrangement ->
                    val possibilites = getTowelsInDesignSortedByLength(design.removePrefix(arrangement))
                    if (possibilites.isEmpty()) return@flatMap emptySet()
                    possibilites.map { arrangement + it }
                }.filter { it.length <= design.length }.toSortedSet().reversed()
                if (arrangments.any { it.length == design.length }) return@count true
            }
            false
        }
        return count
    }

    // credit goes to https://github.com/rnbwdsh
    fun part2(input: List<String>): Int {
        val towels = input[0].split(", ")
        val designs = input.drop(2)

        val arrangementsWorker = MemoizedDeepRecursiveFunction<String, Int> { design: String ->
            if (design.isEmpty()) 1
            else towels.sumOf {
                if (design.startsWith(it)) callRecursive(design.removePrefix(it))
                else 0
            }
        }

        val count = designs.sumOf { design ->
            arrangementsWorker(design)
        }
        return count
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 6)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 16)
    part2(input).println()
}
