package utils

import utils.point.Point
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.pow


private const val resourcesFolder = "src/main/resources"
/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: Int, day: Int, suffix: String = ""): List<String> {
    val dayPadded = day.toString().padStart(2, '0')
    return Path("$resourcesFolder/year$year/day$dayPadded/Day$dayPadded$suffix.txt").readText().trim()
        .lines()
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Return a list of all permutations where values are repeated and ordering is important.
 */
fun <T> List<T>.permutationsWithReplacement(length: Int): List<List<T>> {
    val permutations = size.toDouble().pow(length).toLong()
    val binaryLists = (0 until permutations).map { it.toString(size).padStart(length, '0') }
    return binaryLists.map { it.map { this[it.toString().toInt()] } }
}

/**
 * Return a list of all combinations where values are not repeated and ordering is not important.
 */
fun <T> List<T>.combinations(): List<Pair<T,T>> {
    val combinations = mutableListOf<Pair<T,T>>()
    for (i in indices) {
        for (j in i + 1 until size) {
            combinations.add(this[i] to this[j])
        }
    }
    return combinations
}

fun bfs(graph: Map<Point, List<Point>>, start: Point): Set<Point> {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val vertex = queue.removeFirst()
        if (vertex !in visited) {
            visited.add(vertex)
            graph[vertex]?.let { neighbors -> queue.addAll(neighbors.filterNot { it in visited }) }
        }
    }
    return visited
}

fun bfsPathEnds(graph: Map<Point, List<Point>>, start: Point): List<Point> {
    val queue = ArrayDeque<Point>()
    queue.add(start)
    val pathEnds = mutableListOf<Point>()
    while (queue.isNotEmpty()) {
        val vertex = queue.removeFirst()
        graph[vertex]?.let { neighbors ->
            if (neighbors.isEmpty())
                pathEnds.add(vertex)
            else
                queue.addAll(neighbors) }
    }
    return pathEnds
}

fun concatenatedRegions(locations: List<Point>): List<Set<Point>> {
    val regions = mutableListOf<MutableSet<Point>>()
    for (location in locations) {
        val region = regions.firstOrNull { it.any { it.adjacent(location) } }
        if (region == null) {
            regions.add(mutableSetOf(location))
        } else {
            region.add(location)
        }
    }
    // sanity check
    var inProgress = true
    while (inProgress) {
        inProgress = false
        for (i in regions.indices) {
            for (j in i + 1 until regions.size) {
                if (regions[i].any { first -> regions[j].any { second -> first.adjacent(second) } }) {
                    regions[i].addAll(regions[j])
                    regions.removeAt(j)
                    inProgress = true
                    break
                }
            }
        }
    }
    return regions
}

fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    if (a == 0L) return Triple(b, 0, 1)
    val (gcd, x1, y1) = extendedGcd(b % a, a)
    return Triple(gcd, y1 - (b / a) * x1, x1)
}

fun String.getAllNumbers(): List<Int> {
    return "-?\\d+".toRegex().findAll(this).map { it.value.toInt() }.toList()
}
