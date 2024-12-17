package year2024.day13

import utils.point.LongPoint
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 13

    fun part1(input: List<String>): Int {
        var sum = 0
        for (i in input.indices step 4) {
            val buttonA = Point(input[i].substring(12, 14).toInt(), input[i].substring(18, 20).toInt())
            val buttonB = Point(input[i + 1].substring(12, 14).toInt(), input[i + 1].substring(18, 20).toInt())
            val prize = Point(
                "X=(\\d+),".toRegex().find(input[i + 2])!!.groups[1]!!.value.toInt(),
                "Y=(\\d+)".toRegex().find(input[i + 2])!!.groups[1]!!.value.toInt()
            )
            var solution: Pair<Point, Int>? = null
            var timesB = buttonB.scalarFit(prize)
            while (timesB >= 0) {
                val bPart = buttonB * timesB
                val timesA = buttonA.scalarFit(prize - bPart)
                val aPart = buttonA * timesA
                if (aPart + bPart == prize) {
                    val newSolution = Point(timesA, timesB) to (timesA * 3 + timesB)
                    if (solution == null || newSolution.second < solution.second) {
                        solution = newSolution
                    }
                }
                timesB--
            }
            sum += solution?.second ?: 0
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0.0
        for (i in input.indices step 4) {
            val a = LongPoint(input[i].substring(12, 14).toLong(), input[i].substring(18, 20).toLong())
            val b = LongPoint(input[i + 1].substring(12, 14).toLong(), input[i + 1].substring(18, 20).toLong())
            val prize = LongPoint(
                "X=(\\d+),".toRegex().find(input[i + 2])!!.groups[1]!!.value.toInt() + 10000000000000,
                "Y=(\\d+)".toRegex().find(input[i + 2])!!.groups[1]!!.value.toInt() + 10000000000000
            )
            // u * a.x + v * b.x = prize.x
            // u * a.y + v * b.y = prize.y
            val det = a.x * b.y - a.y * b.x
            val u = (prize.x * b.y - prize.y * b.x) / det
            val v = (a.x * prize.y - a.y * prize.x) / det

            val calculatedPrizeX = a.x * u + b.x * v
            val calculatedPrizeY = a.y * u + b.y * v
            if ((calculatedPrizeX == prize.x) && (calculatedPrizeY == prize.y)) {
                sum += u * 3 + v
            }
        }
        return sum.toLong()
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 480)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 875318608908L)
    part2(input).println()
}
