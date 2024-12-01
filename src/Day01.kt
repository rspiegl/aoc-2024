import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = input.map { it.split("   ").let { it[0] to it[1] } }.unzip()
        val distance = left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l.toInt() - r.toInt()) }
        return distance
    }

    fun part2(input: List<String>): Int {
        val (left, right) = input.map { it.split("   ").let { it[0] to it[1] } }.unzip()
        val rightCounts = right.groupingBy { it }.eachCount()
        val similarityScore = left.sumOf { it.toInt() * (rightCounts[it] ?: 0) }
        return similarityScore
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
