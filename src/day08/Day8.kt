import java.awt.Dimension

fun main() {
    val latestDay = 8

    fun part1(input: List<String>): Int {
        val map = input.toCharMap()
        val antennas = map.getLocations("\\w").map { Antenna(it, map[it.y][it.x]) }
        val groupedAntennas = antennas.groupBy(Antenna::frequency, Antenna::location)
        val antennaCombinations = groupedAntennas.mapValues { it.value.combinations() }
        val antiNodes = antennaCombinations.flatMap { (_, locations) ->
            locations.map { it.getAntinodes() }
        }.flatten().toSet()
        val mapDimension = Dimension(map[0].size, map.size)
        val inBounds = antiNodes.filter { it.inBounds(mapDimension) }
        return inBounds.count()
    }

    fun part2(input: List<String>): Int {
        val map = input.toCharMap()
        val antennas = map.getLocations("\\w").map { Antenna(it, map[it.y][it.x]) }
        val groupedAntennas = antennas.groupBy(Antenna::frequency, Antenna::location)
        val antennaCombinations = groupedAntennas.mapValues { it.value.combinations() }
        val mapDimension = Dimension(map[0].size, map.size)
        val antiNodes = antennaCombinations.flatMap { (_, locations) ->
            locations.map { it.getAntinodesResonantHarmonics(mapDimension) }
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

data class Antenna(val location: Vector2D, val frequency: Char) {
    override fun toString(): String {
        return "$frequency at x=${location.x}, y=${location.y}"
    }
}

fun Pair<Vector2D, Vector2D>.getAntinodes(): Set<Vector2D> {
    val dVector2D = second - first
    val antinodes = setOf(
        first - dVector2D,
        second + dVector2D)
    return antinodes
}

fun Pair<Vector2D, Vector2D>.getAntinodesResonantHarmonics(mapDimension: Dimension): Set<Vector2D> {
    val dVector2D = second - first
    val antinodes = mutableSetOf<Vector2D>()
    var nextAntinode = second
    while (nextAntinode.inBounds(mapDimension)) {
        antinodes.add(nextAntinode)
        nextAntinode += dVector2D
    }
    nextAntinode = first
    while (nextAntinode.inBounds(mapDimension)) {
        antinodes.add(nextAntinode)
        nextAntinode -= dVector2D
    }
    return antinodes
}
