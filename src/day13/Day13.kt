package day13

import utils.extendedGcd
import utils.point.LongPoint
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val latestDay = 13

    fun part1(input: List<String>): Int {
        var sum = 0
        for (i in input.indices step 4) {
            val buttonA = Point(input[i].substring(12, 14).toInt(), input[i].substring(18, 20).toInt())
            val buttonB = Point(input[i + 1].substring(12, 14).toInt(), input[i + 1].substring(18, 20).toInt())
            val prize = Point(
                "X=(\\d+),".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt(),
                "Y=(\\d+)".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt()
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
        var sum = 0L
        for (i in input.indices step 4) {
            val a = LongPoint(input[i].substring(12, 14).toLong(), input[i].substring(18, 20).toLong())
            val b = LongPoint(input[i + 1].substring(12, 14).toLong(), input[i + 1].substring(18, 20).toLong())
            val prize = LongPoint(
                "X=(\\d+),".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt() + 10000000000000,
                "Y=(\\d+)".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt() + 10000000000000
            )
            val (gcd, x0, y0) = extendedGcd(a.x, b.x)
            if (prize.x % gcd != 0L) continue

            val x = prize.x / gcd
            val nx = x0 * x
            val mx = y0 * x
            val k = (prize.y - (nx * a.y + mx * b.y)) / ((b.x * a.y - a.x * b.y) / gcd)
            val n = nx + k * (b.x / gcd)
            val m = mx - k * (a.x / gcd)
            if ((a * n + b * m) == prize) {
                sum += n * 3 + m
            }
        }
        return sum
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 480)
    val input = readInput("$latestDay")
    part1(input).println()

    //check(part2(testInput) == 1L)
    part2(input).println()
}
