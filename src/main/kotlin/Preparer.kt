import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlin.io.path.*

class Preparer {
    private val yearReplaceString = "<YEAR>"
    private val dayReplaceString = "<DAY>"
    private val dayPaddedReplaceString = "<DAY_PADDED>"

    private val codePath = "src/main/kotlin/year%d/day%02d/"
    private val resourcePath = "src/main/resources/year%d/day%02d/"

    suspend fun prepare(year: Int, day: Int) {
        val codePath = codePath.format(year, day, day)
        if (Path(codePath).exists()) {
            println("Day $day already exists.")
            return
        }

        // do early to not create files if puzzleInput fails
        val client = AOCClient(System.getenv("AOC_SESSION"))
        val puzzleInput = client.getPuzzleInput(year, day)

        Path(codePath).createDirectories()
        val dayPadded = day.toString().padStart(2, '0')
        Path("src/main/resources/Template.txt").readText()
            .replace(dayReplaceString, day.toString())
            .replace(dayPaddedReplaceString, dayPadded)
            .replace(yearReplaceString, year.toString())
            .let {
                Path(codePath + "Day$dayPadded.kt").writeText(it)
            }

        val resourcesPath = resourcePath.format(year, day, day)
        Path(resourcesPath).createDirectories()
        Path("${resourcesPath}Day${dayPadded}.txt").writeText(puzzleInput)

        Path("${resourcesPath}Day${dayPadded}_test.txt").createFile()
    }
}

fun main() = runBlocking {
    val year = LocalDate.now().year
    val day = LocalDate.now().dayOfMonth
    Preparer().prepare(year, day)
}
