import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText

fun main() = runBlocking {
    val day = LocalDate.now().dayOfMonth
    val folderPath = "src/day${day.toString().padStart(2, '0')}"
    if (Path(folderPath).exists()) {
        println("Day $day already exists.")
        return@runBlocking
    } else {
        Path(folderPath).createDirectories()
    }

    val filePath = "$folderPath/Day$day"
    val client = AOCClient(System.getenv("AOC_SESSION"))
    val puzzleInput = client.getPuzzleInput(day)
    File("$filePath.txt").writeText(puzzleInput)

    val dayReplaceString = "DAY_NUMBER"
    Path("src/Template.txt").readText().replace(dayReplaceString, day.toString()).let {
        File("$filePath.kt").writeText(it)
    }

    File("${filePath}_test.txt").createNewFile()
}
