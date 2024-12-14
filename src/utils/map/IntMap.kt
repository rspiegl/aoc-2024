package utils.map

import utils.movement.Direction
import utils.point.Point

data class IntMap(val width: Int, val height: Int) {

    var data: Array<IntArray> = Array(height) { IntArray(width) }

    operator fun get(location: Point): Int = data[location.row][location.column]
    operator fun get(column: Int, row: Int): Int = data[row][column]

    fun getLocations(values: Set<Int>): List<Point> {
        val locations = mutableListOf<Point>()
        for (row in 0..<height) {
            for (column in 0..<width) {
                if (data[row][column] in values) {
                    locations.add(Point(column, row))
                }
            }
        }
        return locations
    }

    fun getValues(location: Point, directions: List<Direction>): List<Int> {
        return directions.map { location + it.offset }.filter { it.isInside(width, height) }.map { this[it] }
    }

    fun getValueLocations(): Map<Int, List<Point>> {
        val valueLocations = mutableMapOf<Int, MutableList<Point>>()
        for (row in 0..<height) {
            for (column in 0..<width) {
                valueLocations.computeIfAbsent(this[column, row]) { mutableListOf() }.add(Point(column, row))
            }
        }
        return valueLocations
    }

    fun buildAdjacencyList(): Map<Point, List<Point>> {
        val paths = listOf(Point(-1, 0), Point(1, 0), Point(0, -1), Point(0, 1))
        val adjacencyList = mutableMapOf<Point, List<Point>>()
        for (row in 0..<height) {
            for (column in 0..<width) {
                val currentPoint = Point(column, row)
                val currentHeight = this[column, row]
                val connections = paths.map { currentPoint + it }
                    .filter { it.isInside(width, height) && this[it] == currentHeight + 1 }
                adjacencyList[currentPoint] = connections
            }
        }
        return adjacencyList
    }

    companion object {
        fun of(input: List<String>): IntMap {
            val data = input.map { it.map { it.digitToInt() }.toIntArray() }.toTypedArray()
            return IntMap(data[0].size, data.size).apply { this.data = data }
        }
    }
}
