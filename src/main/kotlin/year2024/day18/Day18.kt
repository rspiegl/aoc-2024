package year2024.day18

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import utils.graph.addEdgeWithVertices
import utils.map.CharMap
import utils.movement.Direction
import utils.point.Point
import utils.println
import utils.readInput


fun main() {
    val year = 2024
    val day = 18

    fun part1(input: List<String>, config: Pair<Point, Int>): Int {
        val corrupted = input.map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
        val dimensions = config.first
        val map = CharMap(dimensions.x, dimensions.y)
        for (i in 0..<config.second)
            map[corrupted[i]] = '#'

        val paths = map.getLocations("\\.")
        val graph = DefaultDirectedGraph<Point, DefaultEdge>(DefaultEdge::class.java)
        paths.forEach { point ->
            Direction.entries.forEach { direction ->
                val nextPoint = point + direction.offset
                if (nextPoint in paths) {
                    graph.addEdgeWithVertices(point, nextPoint)
                }
            }
        }
        val spa = DijkstraShortestPath(graph)
        val pathWeight = spa.getPathWeight(Point(0, 0), dimensions - 1)
        return pathWeight.toInt()
    }

    fun part2(input: List<String>, config: Pair<Point, Int>): String {
        val corrupted = input.map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
        val dimensions = config.first
        val map = CharMap(dimensions.x, dimensions.y)
        var i = 0
        while (i < config.second)
            map[corrupted[i++]] = '#'

        val paths = map.getLocations("\\.")
        val graph = DefaultDirectedGraph<Point, DefaultEdge>(DefaultEdge::class.java)
        paths.forEach { point ->
            Direction.entries.forEach { direction ->
                val nextPoint = point + direction.offset
                if (nextPoint in paths) {
                    graph.addEdgeWithVertices(point, nextPoint)
                }
            }
        }
        val spa = DijkstraShortestPath(graph)
        var point: Point
        while (i < corrupted.size) {
            point = corrupted[i++]
            map[point] = '#'
            graph.removeVertex(point)
            val pathWeight = spa.getPathWeight(Point(0, 0), dimensions - 1)
            if (pathWeight == Double.POSITIVE_INFINITY)
                return "${point.x},${point.y}"
        }
        throw error("Reached End without finding a solution")
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput, Point(7, 7) to 12) == 22)
    val input = readInput(year, day)
    part1(input, Point(71, 71) to 1024).println()

    check(part2(testInput, Point(7, 7) to 12) == "6,1")
    part2(input, Point(71, 71) to 1024).println()
}
