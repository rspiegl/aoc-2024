import java.awt.Dimension
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.absoluteValue
import kotlin.math.pow

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String, suffix: String = "") = Path("src/day${day.padStart(2, '0')}/Day$day$suffix.txt").readText().trim().lines()

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

/**
 * Converts a list of strings to a 2D char array.
 */
fun List<String>.toCharMap(): Array<CharArray> = map { it.toCharArray() }.toTypedArray()

fun List<String>.toIntMap(): Array<IntArray> = map { it.map { it.digitToInt() }.toIntArray() }.toTypedArray()

/**
 * Returns a list of all locations that match the given regex.
 */
fun Array<CharArray>.getLocations(regex: String): List<Vector2D> {
    val toMatch = regex.toRegex()
    val locations = mutableListOf<Vector2D>()
    for (row in indices) {
        for (column in this[row].indices) {
            if (toMatch.matches(this[row][column].toString())) {
                locations.add(Vector2D(column, row))
            }
        }
    }
    return locations
}

/**
 * Returns a list of all locations that match the given value.
 */
fun Array<IntArray>.getLocations(value: Int): List<Vector2D> {
    val locations = mutableListOf<Vector2D>()
    for (row in indices) {
        for (column in this[row].indices) {
            if (this[row][column] == value) {
                locations.add(Vector2D(column, row))
            }
        }
    }
    return locations
}

fun Array<IntArray>.buildAdjacencyList(): Map<Vector2D, List<Vector2D>> {
    val paths = listOf(Vector2D(-1, 0), Vector2D(1, 0), Vector2D(0, -1), Vector2D(0, 1))
    val adjacencyList = mutableMapOf<Vector2D, List<Vector2D>>()
    for (row in indices) {
        for (column in this[row].indices) {
            val currentPoint = Vector2D(column, row)
            val currentHeight = this[row][column]
            val connections = paths.map { currentPoint + it }
                .filter { it.inBounds(Dimension(this[0].size, size)) && this[it.row][it.column] == currentHeight+1 }
            adjacencyList[currentPoint] = connections
        }
    }
    return adjacencyList
}

fun bfs(graph: Map<Vector2D, List<Vector2D>>, start: Vector2D): Set<Vector2D> {
    val visited = mutableSetOf<Vector2D>()
    val queue = ArrayDeque<Vector2D>()
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

fun bfsPathEnds(graph: Map<Vector2D, List<Vector2D>>, start: Vector2D): List<Vector2D> {
    val queue = ArrayDeque<Vector2D>()
    queue.add(start)
    val pathEnds = mutableListOf<Vector2D>()
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

/**
 * A 2D vector class.
 */
data class Vector2D(val column: Int, val row: Int) {

    /**
     * Returns true if the vector is within the bounds of the given dimension.
     */
    fun inBounds(dim: Dimension) = column in 0 until dim.width && row in 0 until dim.height
    fun getLeft() = Vector2D(column - 1, row)
    fun getRight() = Vector2D(column + 1, row)
    fun getUp() = Vector2D(column, row - 1)
    fun getDown() = Vector2D(column, row + 1)


    fun fitsInside(other: Vector2D) = column <= other.column && row <= other.row

    fun scalarFit(other: Vector2D): Int {
        val columnFit = other.column / column
        val rowFit = other.row / row
        return minOf(columnFit, rowFit)
    }

    operator fun times(scalar: Int) = Vector2D(column * scalar, row * scalar)
    operator fun div(scalar: Int) = Vector2D(column / scalar, row / scalar)
    operator fun plus(other: Vector2D) = Vector2D(column + other.column, row + other.row)
    operator fun minus(other: Vector2D) = Vector2D(column - other.column, row - other.row)

    override fun hashCode(): Int {
        return column * 10000 + row
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector2D

        if (column != other.column) return false
        if (row != other.row) return false

        return true
    }

    fun adjacent(point: Vector2D): Boolean {
        return manhattanDistance(point) == 1
    }

    fun manhattanDistance(point: Vector2D): Int {
        return (column - point.column).absoluteValue + (row - point.row).absoluteValue
    }
}

fun Array<CharArray>.getValues(location: Vector2D, directions: List<Direction>): List<Char> {
    return directions.map { location + it.offset }.filter { it.inBounds(Dimension(this[0].size, size)) }.map { this[it.row][it.column] }
}

fun Array<CharArray>.getValueLocations(): Map<Char, List<Vector2D>> {
    val valueLocations = mutableMapOf<Char, MutableList<Vector2D>>()
    for (row in indices) {
        for (column in this[row].indices) {
            valueLocations.computeIfAbsent(this[row][column]) { mutableListOf() }.add(Vector2D(column, row))
        }
    }
    return valueLocations
}

fun Array<CharArray>.get(location: Vector2D): Char {
    return this[location.row][location.column]
}

fun concatenatedRegions(locations: List<Vector2D>): List<Set<Vector2D>> {
    val regions = mutableListOf<MutableSet<Vector2D>>()
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

fun Pair<Long, Long>.scalarFit(other: Pair<Long, Long>): Long {
    val columnFit = other.first / first
    val rowFit = other.second / second
    return minOf(columnFit, rowFit)
}
operator fun Pair<Long, Long>.times(scalar: Long) = Pair(first * scalar, second * scalar)
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = Pair(first + other.first, second + other.second)
operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>) = Pair(first - other.first, second - other.second)

enum class Direction(val offset: Vector2D) {
    NORTH(Vector2D(0, -1)),
    EAST(Vector2D(1, 0)),
    SOUTH(Vector2D(0, 1)),
    WEST(Vector2D(-1, 0));

    fun turnRight() = when(this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turnLeft() = when(this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }
}

fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    if (a == 0L) return Triple(b, 0, 1)
    val (gcd, x1, y1) = extendedGcd(b % a, a)
    return Triple(gcd, y1 - (b / a) * x1, x1)
}
