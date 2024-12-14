package day08

import utils.combinations
import utils.map.CharMap
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val latestDay = 8

    fun part1(input: List<String>): Int {
        val map = CharMap.of(input)
        val antennas = map.getLocations("\\w").map { Antenna(it, map[it]) }
        val groupedAntennas = antennas.groupBy(Antenna::frequency, Antenna::location)
        val antennaCombinations = groupedAntennas.mapValues { it.value.combinations() }
        val antiNodes = antennaCombinations.flatMap { (_, locations) ->
            locations.map { it.getAntinodes() }
        }.flatten().toSet()
        val inBounds = antiNodes.filter { it.isInside(map.width, map.height) }
        return inBounds.count()
    }

    fun part2(input: List<String>): Int {
        val map = CharMap.of(input)
        val antennas = map.getLocations("\\w").map { Antenna(it, map[it]) }
        val groupedAntennas = antennas.groupBy(Antenna::frequency, Antenna::location)
        val antennaCombinations = groupedAntennas.mapValues { it.value.combinations() }
        val antiNodes = antennaCombinations.flatMap { (_, locations) ->
            locations.map { it.getAntinodesResonantHarmonics(map.width, map.height) }
        }.flatten().toSet()
        return antiNodes.count()
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 14)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 34)
    part2(input).println()
}

data class Antenna(val location: Point, val frequency: Char) {
    override fun toString(): String {
        return "$frequency at x=${location.x}, y=${location.y}"
    }
}

fun Pair<Point, Point>.getAntinodes(): Set<Point> {
    val dVector2D = second - first
    val antinodes = setOf(
        first - dVector2D,
        second + dVector2D)
    return antinodes
}

fun Pair<Point, Point>.getAntinodesResonantHarmonics(width: Int, height: Int): Set<Point> {
    val dVector2D = second - first
    val antinodes = mutableSetOf<Point>()
    var nextAntinode = second
    while (nextAntinode.isInside(width, height)) {
        antinodes.add(nextAntinode)
        nextAntinode += dVector2D
    }
    nextAntinode = first
    while (nextAntinode.isInside(width, height)) {
        antinodes.add(nextAntinode)
        nextAntinode -= dVector2D
    }
    return antinodes
}
