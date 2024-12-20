package utils.graph

import org.jgrapht.graph.AbstractBaseGraph
import org.jgrapht.graph.DefaultDirectedWeightedGraph
import utils.movement.Direction
import utils.point.Point

fun <V, E> DefaultDirectedWeightedGraph<V, E>.addEdgeWithWeight(start: V, end: V, weight: Int): E? {
    this.addVertex(start)
    this.addVertex(end)
    val edge = this.addEdge(start, end)
    edge?.let { this.setEdgeWeight(it, weight.toDouble()) }
    return edge
}

fun <V, E> AbstractBaseGraph<V, E>.addEdgeWithVertices(start: V, end: V): E? {
    this.addVertex(start)
    this.addVertex(end)
    val edge = this.addEdge(start, end)
    return edge
}

fun <E> AbstractBaseGraph<Point, E>.addCardinalEdges(point: Point, paths: List<Point>): List<E> {
    return Direction.entries.mapNotNull { direction ->
        val nextPoint = point + direction.offset
        if (nextPoint in paths) {
            val edge = this.addEdgeWithVertices(point, nextPoint)
            edge
        } else
            null
    }
}
