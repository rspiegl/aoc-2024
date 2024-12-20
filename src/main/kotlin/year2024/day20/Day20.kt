package year2024.day20

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import utils.graph.addCardinalEdges
import utils.map.CharMap
import utils.point.Point
import utils.println
import utils.readInput
import kotlin.math.abs
import kotlin.time.measureTime

class RaceCondition(input: List<String>) {
    private val map = CharMap.of(input)
    private val start = map.getLocations("S")[0]
    private val end = map.getLocations("E")[0]
    private val paths = listOf(start) + map.getLocations("\\.") + end
    private val blocks = map.getLocations("#")
    private val stepPerPoint = mutableMapOf<Point, Int>()

    fun part1(cutOff: Double): Int {
        val graph = DefaultUndirectedGraph<Point, DefaultEdge>(DefaultEdge::class.java)
        paths.forEach { point ->
            graph.addCardinalEdges(point, paths)
        }
        val spa = DijkstraShortestPath(graph)
        val originalPathWeight = spa.getPathWeight(start, end)
        val cheatsWithSavings = mutableMapOf<Pair<DefaultEdge, DefaultEdge>, Double>()
        blocks.forEach { block ->
            val edges = graph.addCardinalEdges(block, paths)
            val path = spa.getPath(start, end)
            val weight = path.weight
            val cheat = path.edgeList.filter { it in edges }
            if (cheat.isNotEmpty()) {
                cheatsWithSavings[cheat[0] to cheat[1]] = weight
            }
            edges.forEach { edge ->
                graph.removeEdge(edge)
            }
        }
        val savingsWithNumberOfCheats =
            cheatsWithSavings.values.map { originalPathWeight - it }.groupBy { it }.mapValues { it.value.size }
        return savingsWithNumberOfCheats.filter { it.key >= cutOff }.values.sum()
    }

    fun part2(cutOff: Int): Int {
        // credit goes to u/vbe-elvis
        stepPerPoint.clear()
        val cheats = runWhileCheating(20, cutOff)
        return cheats
    }

    private fun Point.neighboursADistance(currentStep: Int, distance: Int, minimalGain: Int): Int {
        var count = 0
        (-distance..distance).forEach { yOffset ->
            val xDistance = distance - abs(yOffset)
            (-xDistance..xDistance).forEach { xOffset ->
                val offset = Point(xOffset, yOffset)
                stepPerPoint[this + offset]?.let { previousStep ->
                    val savedSteps = currentStep - previousStep - abs(offset.x) - abs(offset.y)
                    if (savedSteps >= minimalGain) count++
                }
            }
        }
        return count
    }

    private fun runWhileCheating(distance: Int, minimalGain: Int): Int {
        var cheatCount = 0
        var step = 0
        var pos: Point? = start
        var prevPos = Point(-1, -1)
        do {
            pos!!
            stepPerPoint[pos] = step
            cheatCount += pos.neighboursADistance(step, distance, minimalGain)

            step++
            val previous = pos
            pos = map.next(pos, prevPos)
            prevPos = previous
        } while (pos != null)
        return cheatCount
    }
}

fun main() {
    val year = 2024
    val day = 20

    val solutionTest = RaceCondition(readInput(year, day, suffix = "_test"))
    check(solutionTest.part1(12.0) == 8)
    val solution = RaceCondition(readInput(year, day))
    measureTime { print("Part 1 result ${solution.part1(100.0)} in ") }.println()

    check(solutionTest.part2(50) == 285)
    measureTime { print("Part 2 result ${solution.part2(100)} in ") }.println()
}
