package utils.map

import utils.movement.Direction
import utils.point.Point

@Suppress("unused")
data class CharMap(val width: Int, val height: Int) {

    var data: Array<CharArray> = Array(height) { CharArray(width) { '.' } }

    operator fun get(location: Point): Char = data[location.row][location.column]
    operator fun get(column: Int, row: Int): Char = data[row][column]
    operator fun get(locations: Iterable<Point>): List<Char> = locations.map { this[it] }


    operator fun set(at: Point, value: Char) {
        data[at.row][at.column] = value
    }

    fun getLocations(regex: String): List<Point> {
        val toMatch = regex.toRegex()
        val locations = mutableListOf<Point>()
        for (row in 0..<height) {
            for (column in 0..<width) {
                if (toMatch.matches(this[column, row].toString())) {
                    locations.add(Point(column, row))
                }
            }
        }
        return locations
    }

    fun getValues(location: Point, directions: List<Direction>): List<Char> {
        return directions.map { location + it.offset }.filter { it.isInside(width, height) }.map { this[it] }
    }

    fun getValueLocations(): Map<Char, List<Point>> {
        val valueLocations = mutableMapOf<Char, MutableList<Point>>()
        for (row in 0..<height) {
            for (column in 0..<width) {
                valueLocations.computeIfAbsent(data[row][column]) { mutableListOf() }.add(Point(column, row))
            }
        }
        return valueLocations
    }

    fun findVerticalLines(char: Char, longerThan: Int): Int {
        val verticalLines = mutableListOf<Int>()
        for (column in 0..<width) {
            val spans = mutableListOf(0)
            for (row in 0..<height) {
                if (this[column, row] == char) {
                    spans[spans.lastIndex] = spans.last() + 1
                } else {
                    spans.add(0)
                }
            }
            verticalLines.add(spans.max())
        }
        return verticalLines.count { it >= longerThan }
    }

    fun printable(): String {
        return data.joinToString("\n") { it.joinToString("") }
    }

    companion object {
        fun of(input: List<String>): CharMap {
            val data = input.map { it.toCharArray() }.toTypedArray()
            return CharMap(data[0].size, data.size).apply { this.data = data }
        }

        fun of(array: Array<CharArray>): CharMap {
            return CharMap(array[0].size, array.size).apply { this.data = array }
        }
    }
}
