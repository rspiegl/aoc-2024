import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText

fun main() = runBlocking {
    val year = LocalDate.now().year
    val day = LocalDate.now().dayOfMonth
    val dayPadded = day.toString().padStart(2, '0')
    val folderPath = "src/main/kotlin/year$year/day$dayPadded"
    val filePath = "$folderPath/Day$dayPadded"
    val client = AOCClient(System.getenv("AOC_SESSION"))

    if (Path(folderPath).exists()) {
        println("Day $day already exists.")
        return@runBlocking
    } else {
        Path(folderPath).createDirectories()
    }

    val puzzleInput = client.getPuzzleInput(year, day)
    File("$filePath.txt").writeText(puzzleInput)

    val yearReplaceString = "<YEAR>"
    val dayReplaceString = "<DAY>"
    Path("src/main/resources/Template.txt").readText()
        .replace(dayReplaceString, dayPadded)
        .replace(yearReplaceString, year.toString()).let {
        File("$filePath.kt").writeText(it)
    }

    File("${filePath}_test.txt").createNewFile()
}
