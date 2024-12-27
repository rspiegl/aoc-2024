package year2024.day25

import utils.map.CharMap
import utils.println
import utils.readInput
import kotlin.time.measureTime

class CodeChronicle(private val input: List<String>) {

    fun part1(): Int {
        val schematics = input.splitBy { it.isEmpty() }.map { CharMap.of(it) }
        val (locks, keys) = schematics.partition { it.isLock() }
        val lockPins = locks.map { it.countVertically('#') }
        val keyPins = keys.map { it.countVertically('#') }
        val overlaps = lockPins.sumOf { lockPin ->
            keyPins.count { keyPin ->
                keyPin.zip(lockPin).all { it.first + it.second <= 5 }
            }
        }
        return overlaps
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val year = 2024
    val day = 25

    val solutionTest = CodeChronicle(readInput(year, day, suffix = "_test"))
    check(solutionTest.part1() == 3)
    val solution = CodeChronicle(readInput(year, day))
    measureTime { print("Part 1 result ${solution.part1()} in ") }.println()

    check(solutionTest.part2() == 0)
    measureTime { print("Part 2 result ${solution.part2()} in ") }.println()
}

fun CharMap.countVertically(char: Char): List<Int> {
    val counts = mutableListOf<Int>()
    repeat(this.width) { x ->
        var count = 0
        repeat(this.height) { y ->
            if (this[x, y] == char) count++
        }
        counts.add(count - 1)
    }
    return counts
}

fun CharMap.isLock(): Boolean {
    return this.data[0].all { it == '#' }
}

fun <T> List<T>.splitBy(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()
    var current = mutableListOf<T>()
    forEach {
        if (predicate(it)) {
            result.add(current)
            current = mutableListOf()
        } else {
            current.add(it)
        }
    }
    result.add(current)
    return result
}
