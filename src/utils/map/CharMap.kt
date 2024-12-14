package utils.map

import utils.movement.Direction
import utils.point.Point

data class CharMap(val width: Int, val height: Int) {

    var data: Array<CharArray> = Array(height) { CharArray(width) }

    operator fun get(location: Point): Char = data[location.row][location.column]
    operator fun get(column: Int, row: Int): Char = data[row][column]

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

    companion object {
        fun of(input: List<String>): CharMap {
            val data = input.map { it.toCharArray() }.toTypedArray()
            return CharMap(data[0].size, data.size).apply { this.data = data }
        }
    }
}
