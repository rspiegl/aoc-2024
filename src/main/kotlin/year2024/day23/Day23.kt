package year2024.day23

import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.alg.cycle.HawickJamesSimpleCycles
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.jgrapht.graph.SimpleGraph
import utils.graph.addEdgeWithVertices
import utils.println
import utils.readInput
import kotlin.time.measureTime


class LANParty(input: List<String>) {

    private val directedGraph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java).also {
        input.forEach { line ->
            val (pc1, pc2) = line.split("-")
            it.addEdgeWithVertices(pc1, pc2)
            it.addEdgeWithVertices(pc2, pc1)
        }
    }
    private val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java).also {
        input.forEach { line ->
            val (pc1, pc2) = line.split("-")
            it.addEdgeWithVertices(pc1, pc2)
        }
    }

    fun part1(): Int {
        val hjsc = HawickJamesSimpleCycles(directedGraph)
        hjsc.setPathLimit(3)
        val simpleCycles = hjsc.findSimpleCycles()
        val sets =
            simpleCycles.filter { it.size == 3 && it.any { pc -> pc.startsWith("t") } }.map { it.sorted() }.distinct()
        return sets.size
    }

    fun part2(): String {
        return BronKerboschCliqueFinder(graph).maximumIterator().next().sorted().joinToString(",")
    }
}

fun main() {
    val year = 2024
    val day = 23

    val solutionTest = LANParty(readInput(year, day, suffix = "_test"))
    check(solutionTest.part1() == 7)
    val solution = LANParty(readInput(year, day))
    measureTime { print("Part 1 result ${solution.part1()} in ") }.println()

    check(solutionTest.part2() == "co,de,ka,ta")
    measureTime { print("Part 2 result ${solution.part2()} in ") }.println()
}
