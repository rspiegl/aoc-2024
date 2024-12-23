package year2024.day23

import org.jgrapht.alg.cycle.HawickJamesSimpleCycles
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import utils.graph.addEdgeWithVertices
import utils.println
import utils.readInput
import java.util.*
import kotlin.time.measureTime


class LANParty(input: List<String>) {

    private val directedGraph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java).also {
        input.forEach { line ->
            val (pc1, pc2) = line.split("-")
            it.addEdgeWithVertices(pc1, pc2)
            it.addEdgeWithVertices(pc2, pc1)
        }
    }
    private val network = UndirectedMap(input.map { it.split("-") })

    fun part1(): Int {
        val hjsc = HawickJamesSimpleCycles(directedGraph)
        hjsc.setPathLimit(3)
        val simpleCycles = hjsc.findSimpleCycles()
        val sets =
            simpleCycles.filter { it.size == 3 && it.any { pc -> pc.startsWith("t") } }.map { it.sorted() }.distinct()
        return sets.size
    }

    fun part2(): String {
        val hjsc = HawickJamesSimpleCycles(directedGraph)
        hjsc.setPathLimit(3)
        val parties = hjsc.findSimpleCycles().filter { it.size == 3 && it.any { pc -> pc.startsWith("t") } }
            .map { it.toMutableSet() }.toSet()
        parties.forEach { party ->
            val toCheck: Queue<String> = LinkedList(party)
            while (toCheck.isNotEmpty()) {
                val pc = toCheck.poll()
                val neighbours = network.map[pc] ?: emptySet()
                val newNeighbours = neighbours - party
                newNeighbours.forEach { newPc ->
                    val connected = party.all { newPc in (network.map[it] ?: emptySet()) }
                    if (connected) {
                        party.add(newPc)
                        toCheck.add(newPc)
                    }
                }
            }

        }
        return parties.maxBy { it.size }.sorted().joinToString(",")
    }
}

class UndirectedMap(list: List<List<String>>) {
    val map = mutableMapOf<String, Set<String>>().apply {
        list.forEach { (pc1, pc2) ->
            this[pc1] = this.getOrDefault(pc1, emptySet()) + pc2
            this[pc2] = this.getOrDefault(pc2, emptySet()) + pc1
        }
    }.toMap()
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
