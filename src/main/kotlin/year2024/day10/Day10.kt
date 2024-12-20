package year2024.day10

import utils.bfs
import utils.bfsPathEnds
import utils.map.IntMap
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 10

    fun part1(input: List<String>): Int {
        val topography = IntMap.of(input)
        val hikingStarts = topography.getLocations(setOf(0))
        val hikingEnds = topography.getLocations(setOf(9))
        val adjacencyList = topography.buildAdjacencyList()
        val sum = hikingStarts.sumOf { hikingStart ->
            val visited = bfs(adjacencyList, hikingStart)
            val visitedEnds = visited.filter { it in hikingEnds }
            visitedEnds.size
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val topography = IntMap.of(input)
        val hikingStarts = topography.getLocations(setOf(0))
        val hikingEnds = topography.getLocations(setOf(9))
        val adjacencyList = topography.buildAdjacencyList()
        val sum = hikingStarts.sumOf { hikingStart ->
            val pathEnds = bfsPathEnds(adjacencyList, hikingStart)
            val visitedEnds = pathEnds.filter { it in hikingEnds }
            visitedEnds.size
        }
        return sum
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 2)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 227)
    part2(input).println()
}
