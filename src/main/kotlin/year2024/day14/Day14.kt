package year2024.day14

import utils.getAllNumbers
import utils.map.CharMap
import utils.point.Point
import utils.point.Rectangle
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 14

    fun part1(input: List<String>, point: Point): Int {
        val robots = input.map {
            val numbers = it.getAllNumbers()
            Robot(Point(numbers[0], numbers[1]), Point(numbers[2], numbers[3]))
        }
        for (i in 0 until 100) {
            robots.forEach { it.move(point) }
        }
        val quadrants = point.quadrants().associateWith { 0 }.toMutableMap()
        robots.forEach { robot ->
            val quadrant = quadrants.entries.find { (area, _) -> robot.position in area }
            quadrant?.let { it.setValue(it.value + 1) }
        }
        return quadrants.values.reduce(Int::times)
    }

    fun part2(input: List<String>, point: Point): Int {
        val robots = input.map {
            val numbers = it.getAllNumbers()
            Robot(Point(numbers[0], numbers[1]), Point(numbers[2], numbers[3]))
        }
        var i = 0
        var cont = "y"
        while (cont != "n") {
            robots.forEach { it.move(point) }
            i++
            val grid = makeGrid(point, robots.map { it.position })
            val verticalLines = grid.findVerticalLines('#', 5)
            if (verticalLines >= 2) {
                print(grid.printable())
                print("current iteration is $i, continue? (y/n): ")
                cont = readln()
            }
        }
        return i
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput, Point(11, 7)) == 12)
    val input = readInput(year, day)
    part1(input, Point(101, 103)).println()

    // check(part2(testInput, Dimension(11, 7)) == 1)
    part2(input, Point(101, 103)).println()
}

fun makeGrid(point: Point, positions: List<Point>): CharMap {
    val grid = Array(point.height) { CharArray(point.width) { '.' } }
    positions.forEach { grid[it.y][it.x] = '#' }
    return CharMap.of(grid)
}

data class Robot(var position: Point, val velocity: Point) {
    fun move(point: Point) {
        position += velocity
        position = position.wrap(point)
    }
}

fun Point.quadrants(): List<Rectangle> {
    val halfWidth = width / 2
    val halfHeight = height / 2
    return listOf(
        Rectangle(Point(0, 0), Point(halfWidth, halfHeight)),
        Rectangle(Point(halfWidth + 1, 0), Point(width, halfHeight)),
        Rectangle(Point(0, halfHeight + 1), Point(halfWidth, height)),
        Rectangle(Point(halfWidth + 1, halfHeight + 1), Point(width, height))
    )
}
