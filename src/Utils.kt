import java.awt.Dimension
import kotlin.io.path.Path
import kotlin.io.path.readText
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
 * A 2D vector class.
 */
data class Vector2D(val x: Int, val y: Int) {

    /**
     * Returns true if the vector is within the bounds of the given dimension.
     */
    fun inBounds(dim: Dimension) = x in 0 until dim.width && y in 0 until dim.height

    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
}
