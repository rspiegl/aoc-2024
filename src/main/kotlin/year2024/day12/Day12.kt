package year2024.day12

import utils.concatenatedRegions
import utils.map.CharMap
import utils.movement.Direction
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 12

    fun part1(input: List<String>): Int {
        val map = CharMap.of(input)
        val garden = map.getValueLocations().mapValues { concatenatedRegions(it.value) }
        val newplots = garden.map { (char, regions) ->
            val plotss = regions.map { region ->
                val area = region.size
                val perimeter = region.sumOf { location ->
                    Direction.entries.count { direction ->
                        if ((location + direction.offset).isInside(map.width, map.height))
                            map[location + direction.offset] != map[location]
                        else
                            true
                    }
                }
                Plot(area, perimeter, region.toMutableList())
            }
            char to plotss
        }
        val sum = newplots.sumOf { it.second.sumOf { plot -> plot.price() } }
        return sum
    }

    fun part2(input: List<String>): Int {
        val map = CharMap.of(input)
        val garden = map.getValueLocations().mapValues { concatenatedRegions(it.value) }
        val newplots = garden.map { (char, regions) ->
            val plotss = regions.map { region ->
                val area = region.size
                val perimeter = region.sumOf { location ->
                    Direction.entries.count { direction ->
                        if ((location + direction.offset).isInside(map.width, map.height))
                            map[location + direction.offset] != map[location]
                        else
                            true
                    }
                }
                Plot(area, perimeter, region.toMutableList())
            }
            char to plotss
        }
        val sum = newplots.sumOf { it.second.sumOf { plot -> plot.price2() } }
        return sum
    }

    val testInput = readInput(year, day, suffix = "_test")
    //check(part1(testInput) == 1930)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 436)
    part2(input).println()
}

class Plot(var area: Int, var perimeter: Int, val inside: MutableList<Point> = mutableListOf()) {
    fun price() = area * perimeter

    fun price2() = area * sides()

    fun isInside(location: Point): Boolean {
        return location in inside
    }

    fun sides(): Int {
        val minX = inside.minOf { it.x }
        val maxX = inside.maxOf { it.x }
        val minY = inside.minOf { it.y }
        val maxY = inside.maxOf { it.y }

        val visitedSides = mutableMapOf(
            "LEFT" to mutableSetOf<Point>(),
            "RIGHT" to mutableSetOf(),
            "UP" to mutableSetOf(),
            "DOWN" to mutableSetOf()
        )
        var sides = 0
        for (row in minY..maxY) {
            for (column in minX..maxX) {
                val location = Point(column, row)
                if (isInside(location)) {
                    val left = location.left()
                    if (!isInside(left)) {
                        if (left.up() !in visitedSides["LEFT"]!! && left.down() !in visitedSides["LEFT"]!!) {
                            sides++
                        }
                        visitedSides["LEFT"]!!.add(left)
                    }

                    val right = location.right()
                    if (!isInside(right)) {
                        if (right.up() !in visitedSides["RIGHT"]!! && right.down() !in visitedSides["RIGHT"]!!) {
                            sides++
                        }
                        visitedSides["RIGHT"]!!.add(right)
                    }

                    val up = location.up()
                    if (!isInside(up)) {
                        if (up.left() !in visitedSides["UP"]!! && up.right() !in visitedSides["UP"]!!) {
                            sides++
                        }
                        visitedSides["UP"]!!.add(up)
                    }

                    val down = location.down()
                    if (!isInside(down)) {
                        if (down.left() !in visitedSides["DOWN"]!! && down.right() !in visitedSides["DOWN"]!!) {
                            sides++
                        }
                        visitedSides["DOWN"]!!.add(down)
                    }
                }
            }
        }
        return sides
    }
}
