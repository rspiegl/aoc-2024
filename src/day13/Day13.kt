fun main() {
    val latestDay = 13

    fun part1(input: List<String>): Int {
        var sum = 0
        for (i in input.indices step 4) {
            val buttonA = Vector2D(input[i].substring(12, 14).toInt(), input[i].substring(18, 20).toInt())
            val buttonB = Vector2D(input[i+1].substring(12, 14).toInt(), input[i+1].substring(18, 20).toInt())
            val prize = Vector2D(
                "X=(\\d+),".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt(),
                "Y=(\\d+)".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt()
            )
            var solution: Pair<Vector2D, Int>? = null
            var timesB = buttonB.scalarFit(prize)
            while (timesB >= 0) {
                val bPart = buttonB * timesB
                val timesA = buttonA.scalarFit(prize - bPart)
                val aPart = buttonA * timesA
                if (aPart + bPart == prize) {
                    val newSolution = Vector2D(timesA, timesB) to (timesA*3 + timesB)
                    if (solution == null || newSolution.second < solution.second) {
                        solution = newSolution
                    }
                }
                timesB--
            }
            sum += solution?.second ?: 0
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        for (i in input.indices step 4) {
            val a = Pair(input[i].substring(12, 14).toLong(), input[i].substring(18, 20).toLong())
            val b = Pair(input[i+1].substring(12, 14).toLong(), input[i+1].substring(18, 20).toLong())
            val prize = Pair(
                "X=(\\d+),".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt() + 10000000000000,
                "Y=(\\d+)".toRegex().find(input[i+2])!!.groups[1]!!.value.toInt() + 10000000000000
            )
            val (gcd, x0, y0) = extendedGcd(a.first, b.first)
            if (prize.first % gcd != 0L) continue

            val x = prize.first / gcd
            val nx = x0 * x
            val mx = y0 * x
            val k = (prize.second - (nx * a.second + mx * b.second)) / ((b.first * a.second - a.first * b.second) / gcd)
            val n = nx + k * (b.first / gcd)
            val m = mx - k * (a.first / gcd)
            if (n * a.first + m *b.first == prize.first && n * a.second + m * b.second == prize.second) {
                sum += n * 3 + m
            }
        }
        return sum
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 480)
    val input = readInput("$latestDay")
    part1(input).println()

    //check(part2(testInput) == 1L)
    part2(input).println()
}
