import java.awt.Dimension

fun main() {
    val latestDay = 12

    fun part1(input: List<String>): Int {
        val map = input.toCharMap()
        val garden = map.getValueLocations().mapValues { concatenatedRegions(it.value) }
        val newplots = garden.map { (char, regions) ->
            val plotss = regions.map { region ->
                val area = region.size
                val perimeter = region.sumOf { location ->
                    Direction.entries.count { direction ->
                        if ((location+direction.offset).inBounds(Dimension(map[0].size, map.size)))
                            map.get(location + direction.offset) != map.get(location)
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
        val map = input.toCharMap()
        val garden = map.getValueLocations().mapValues { concatenatedRegions(it.value) }
        val newplots = garden.map { (char, regions) ->
            val plotss = regions.map { region ->
                val area = region.size
                val perimeter = region.sumOf { location ->
                    Direction.entries.count { direction ->
                        if ((location+direction.offset).inBounds(Dimension(map[0].size, map.size)))
                            map.get(location + direction.offset) != map.get(location)
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

    val testInput = readInput("$latestDay", suffix = "_test")
    //check(part1(testInput) == 1930)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 436)
    part2(input).println()
}

class Plot(var area: Int, var perimeter: Int, val inside: MutableList<Vector2D> = mutableListOf()) {
    fun price() = area * perimeter

    fun price2() = area * sides()

    fun isInside(location: Vector2D): Boolean {
        return location in inside
    }

    fun sides(): Int {
        val minX = inside.minOf { it.column }
        val maxX = inside.maxOf { it.column }
        val minY = inside.minOf { it.row }
        val maxY = inside.maxOf { it.row }

        val visitedSides = mutableMapOf("LEFT" to mutableSetOf<Vector2D>(), "RIGHT" to mutableSetOf(), "UP" to mutableSetOf(), "DOWN" to mutableSetOf())
        var sides = 0
        for (row in minY..maxY) {
            for (column in minX..maxX) {
                val location = Vector2D(column, row)
                if (isInside(location)) {
                    val left = location.getLeft()
                    if (!isInside(left)) {
                        if (left.getUp() !in visitedSides["LEFT"]!! && left.getDown() !in visitedSides["LEFT"]!!) {
                            sides++
                        }
                        visitedSides["LEFT"]!!.add(left)
                    }

                    val right = location.getRight()
                    if (!isInside(right)) {
                        if (right.getUp() !in visitedSides["RIGHT"]!! && right.getDown() !in visitedSides["RIGHT"]!!) {
                            sides++
                        }
                        visitedSides["RIGHT"]!!.add(right)
                    }

                    val up = location.getUp()
                    if (!isInside(up)) {
                        if (up.getLeft() !in visitedSides["UP"]!! && up.getRight() !in visitedSides["UP"]!!) {
                            sides++
                        }
                        visitedSides["UP"]!!.add(up)
                    }

                    val down = location.getDown()
                    if (!isInside(down)) {
                        if (down.getLeft() !in visitedSides["DOWN"]!! && down.getRight() !in visitedSides["DOWN"]!!) {
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
