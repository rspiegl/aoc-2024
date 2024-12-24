package year2024.day24

import utils.println
import utils.readInput
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.time.measureTime

class CrossedWires(private val input: List<String>) {

    fun part1(): Long {
        val (starts, gates) = input.partition { ":" in it }
        val values = starts.associate { start -> start.split(": ").let { it[0] to it[1].toByte() } }.toMutableMap()
        val result = simulateCircuit(gates, values)
        val binary = result.getBinary("z")
        return binary.toLong(2)
    }

    private fun Map<String, Byte>.getBinary(prefix: String) =
        filterKeys { it.startsWith(prefix) }.entries.sortedWith(compareByDescending { it.key })
            .joinToString("") { it.value.toString() }

    fun part2(): String {
        val (starts, gates) = input.partition { ":" in it }
        val values = starts.associate { start -> start.split(": ").let { it[0] to it[1].toByte() } }.toMutableMap()
        // looking at graph and manually replacing following output pairs
        // hnd z09 tdv z16 bks z23 tjp nrn
        val swaps = listOf("hnd", "z09", "tdv", "z16", "bks", "z23", "tjp", "nrn")
        val result = simulateCircuit(gates, values)
        val x = result.getBinary("x")
        val y = result.getBinary("y")
        val z = result.getBinary("z")
        val zShouldBe = (x.toLong(2) + y.toLong(2)).toString(2)
        assert(z == zShouldBe)
        return swaps.sorted().joinToString(",")
    }

    private fun simulateCircuit(
        gates: List<String>,
        values: Map<String, Byte>
    ): Map<String, Byte> {
        val result = values.toMutableMap()
        val regex = """([a-z\d]{3}).+([a-z\d]{3}).+([a-z\d]{3})""".toRegex()
        var hasChanged = true
        while (hasChanged) {
            hasChanged = false
            gates.drop(1).forEach { gate ->
                val (first, second, target) = regex.find(gate)!!.destructured
                if (target in result || first !in result || second !in result) return@forEach
                when {
                    "XOR" in gate -> {
                        result[target] = result[first]!! xor result[second]!!
//                        println("$first -> $target [label=\"${result[first]!!} XOR\"];")
//                        println("$second -> $target [label=\"${result[second]!!} XOR\"];")
                        hasChanged = true
                    }

                    "AND" in gate -> {
                        result[target] = result[first]!! and result[second]!!
//                        println("$first -> $target [label=\"${result[first]!!} AND\"];")
//                        println("$second -> $target [label=\"${result[second]!!} AND\"];")
                        hasChanged = true
                    }

                    "OR" in gate -> {
                        result[target] = result[first]!! or result[second]!!
//                        println("$first -> $target [label=\"${result[first]!!} OR\"];")
//                        println("$second -> $target [label=\"${result[second]!!} OR\"];")
                        hasChanged = true
                    }
                }
            }
        }
        return result
    }
}

fun main() {
    val year = 2024
    val day = 24

    val solutionTest = CrossedWires(readInput(year, day, suffix = "_test"))
    check(solutionTest.part1() == 2024L)
    val solution = CrossedWires(readInput(year, day))
    measureTime { print("Part 1 result ${solution.part1()} in ") }.println()

    measureTime { print("Part 2 result ${solution.part2()} in ") }.println()
}
