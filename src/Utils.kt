import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.pow

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String, suffix: String = "") = Path("src/day${day.padStart(2, '0')}/Day$day$suffix.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> List<T>.permutationsWithReplacement(length: Int): List<List<T>> {
    val permutations = size.toDouble().pow(length).toLong()
    val binaryLists = (0 until permutations).map { it.toString(size).padStart(length, '0') }
    return binaryLists.map { it.map { this[it.toString().toInt()] } }
}

fun Int.factorial() = (1..this).fold(1, Int::times)
