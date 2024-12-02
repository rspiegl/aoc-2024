import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.time.LocalDate
import kotlin.io.path.*

fun main() {
    val todaysDay = LocalDate.now().dayOfMonth
    val folderPath = "src/day${todaysDay.toString().padStart(2, '0')}"
    if (Path(folderPath).exists()) {
        println("Day $todaysDay already exists.")
        return
    } else {
        Path(folderPath).createDirectories()
    }

    val filePath = "$folderPath/Day$todaysDay"
    OkHttpClient().newCall(
        Request.Builder()
            .get()
            .url("https://adventofcode.com/2024/day/$todaysDay/input")
            .addHeader("Cookie", "session=YOUR_SESSION_COOKIE")
            .build()
    ).execute().use { response ->
        if (!response.isSuccessful) throw RuntimeException("Failed to fetch input: $response")
        File("$filePath.txt").writeText(response.body!!.string())
    }

    val dayReplaceString = "DAY_NUMBER"
    Path("src/Template.txt").readText().replace(dayReplaceString, todaysDay.toString()).let {
        File("$filePath.kt").writeText(it)
    }

    File("${filePath}_test.txt").createNewFile()
}
