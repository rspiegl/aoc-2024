package year2024.day16

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.alg.shortestpath.YenKShortestPath
import org.jgrapht.graph.DefaultDirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import utils.graph.addEdgeWithWeight
import utils.map.CharMap
import utils.movement.Direction
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 16

    fun part1(input: List<String>): Int {
        val map = CharMap.of(input)
        val start = map.getLocations("S")[0]
        val end = map.getLocations("E")[0]
        val paths = map.getLocations("[\\.SE]")
        val graph = DefaultDirectedWeightedGraph<DirectedPoint, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        val startDirectedPoint = DirectedPoint(start, Direction.EAST)
        val endDirectedPoint = DirectedPoint(end, Direction.EAST)
        graph.addVertex(startDirectedPoint)
        graph.addVertex(endDirectedPoint)
        Direction.entries.forEach {
            graph.addEdgeWithWeight(DirectedPoint(end, it), endDirectedPoint, 0)
        }
        paths.forEach { point ->
            Direction.entries.forEach { direction ->
                val nextPoint = point + direction.offset
                if (nextPoint in paths) {
                    graph.addEdgeWithWeight(DirectedPoint(point, direction), DirectedPoint(nextPoint, direction), 1)
                }
                graph.addEdgeWithWeight(
                    DirectedPoint(point, direction),
                    DirectedPoint(point, direction.turnRight()),
                    1000
                )
                graph.addEdgeWithWeight(
                    DirectedPoint(point, direction),
                    DirectedPoint(point, direction.turnLeft()),
                    1000
                )
            }
        }
        val spa = DijkstraShortestPath(graph)
        val pathWeight = spa.getPathWeight(startDirectedPoint, endDirectedPoint)
        return pathWeight.toInt()
    }

    fun part2(input: List<String>): Int {
        val map = CharMap.of(input)
        val start = map.getLocations("S")[0]
        val end = map.getLocations("E")[0]
        val points = map.getLocations("[\\.SE]")
        val graph = DefaultDirectedWeightedGraph<DirectedPoint, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        val startDirectedPoint = DirectedPoint(start, Direction.EAST)
        val endDirectedPoint = DirectedPoint(end, Direction.EAST)
        graph.addVertex(startDirectedPoint)
        Direction.entries.forEach {
            graph.addEdgeWithWeight(DirectedPoint(end, it), endDirectedPoint, 0)
        }
        points.forEach { point ->
            Direction.entries.forEach { direction ->
                val nextPoint = point + direction.offset
                if (nextPoint in points) {
                    graph.addEdgeWithWeight(DirectedPoint(point, direction), DirectedPoint(nextPoint, direction), 1)
                }
                graph.addEdgeWithWeight(
                    DirectedPoint(point, direction),
                    DirectedPoint(point, direction.turnRight()),
                    1000
                )
                graph.addEdgeWithWeight(
                    DirectedPoint(point, direction),
                    DirectedPoint(point, direction.turnLeft()),
                    1000
                )
            }
        }
        val spa = DijkstraShortestPath(graph)
        val pathWeight = spa.getPathWeight(startDirectedPoint, endDirectedPoint)

        val yenK = YenKShortestPath(graph).getPaths(startDirectedPoint, endDirectedPoint, 25)
        val shortestPaths = yenK.filter { it.weight == pathWeight }
        val uniquePoints = shortestPaths.fold(mutableSetOf<Point>()) { acc, path ->
            acc.addAll(path.vertexList.map { it.point })
            acc
        }
        return uniquePoints.size
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 11048)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 64)
    part2(input).println()
}

data class DirectedPoint(val point: Point, val direction: Direction) {
    override fun hashCode(): Int {
        return point.hashCode() * 10 + direction.ordinal
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DirectedPoint

        if (point != other.point) return false
        if (direction != other.direction) return false

        return true
    }
}
