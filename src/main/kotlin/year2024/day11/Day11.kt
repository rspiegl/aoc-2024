package year2024.day11

import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 11

    fun applyBlink(stones: MutableMap<Long, Long>): MutableMap<Long, Long> {
        var stones1 = stones
        val result = mutableMapOf<Long, Long>()
        stones1.forEach { (stone, count) ->
            when {
                stone == 0L -> result.compute(1L) { _, v -> (v ?: 0L) + count }
                stone.toString().length % 2 == 0 -> {
                    val str = stone.toString()
                    val half = str.length / 2
                    val first = str.substring(0, half).toLong()
                    val second = str.substring(half).toLong()
                    result.compute(first) { _, v -> (v ?: 0L) + count }
                    result.compute(second) { _, v -> (v ?: 0L) + count }
                }

                else -> result.compute(stone * 2024) { _, v -> (v ?: 0L) + count }
            }
        }
        stones1 = result
        return stones1
    }

    fun part1(input: List<String>): Long {
        var stones = input[0].split(" ").associate { it.toLong() to 1L }.toMutableMap()
        for (blinks in 0..<25) {
            stones = applyBlink(stones)
        }
        return stones.values.sum()
    }

    fun part2(input: List<String>): Long {
        var stones = input[0].split(" ").associate { it.toLong() to 1L }.toMutableMap()
        for (blinks in 0..<75) {
            stones = applyBlink(stones)
        }
        return stones.values.sum()
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 55312L)
    val input = readInput(year, day)
    part1(input).println()

    // check(part2(testInput) == 1)
    part2(input).println()
}
