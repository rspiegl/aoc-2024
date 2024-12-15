import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class AOCClient(private val sessionCookie: String?) {
    private val client = HttpClient(CIO) { }

    private val aoc = "https://adventofcode.com"
    private val userAgent = "Mozilla/5.0"


    suspend fun getPuzzleInput(year: Int, day: Int): String {
        if (sessionCookie == null) {
            throw Exception("No session cookie provided")
        }
        return client.get("$aoc/$year/day/$day/input") {
            headers {
                append("Cookie", "session=$sessionCookie")
                append("User-Agent", userAgent)
            }
        }.bodyAsText()
    }
}
