fun main() {
    val latestDay = 4

    fun Array<CharArray>.apply(strategy: XmasStrategy): Int {
        var count = 0
        for (row in strategy.rowMinOffset until this.size - strategy.rowMaxOffset) {
            for (column in strategy.columnMinOffset until this[row].size - strategy.columnMaxOffset) {
                if (this[strategy.rowOperation(row, 0)][strategy.columnOperation(column, 0)] == 'X' &&
                    this[strategy.rowOperation(row, 1)][strategy.columnOperation(column, 1)] == 'M' &&
                    this[strategy.rowOperation(row, 2)][strategy.columnOperation(column, 2)] == 'A' &&
                    this[strategy.rowOperation(row, 3)][strategy.columnOperation(column, 3)] == 'S') {
                    count++
                }
            }
        }
        return count
    }
    val right = XmasStrategy(0, 0, 0, 3, { row, _ -> row }, { column, offset -> column + offset })
    val left = XmasStrategy(0, 0, 3, 0, { row, _ -> row }, { column, offset -> column - offset })
    val up = XmasStrategy(3, 0, 0, 0, { row, offset -> row - offset }, { column, _ -> column })
    val down = XmasStrategy(0, 3, 0, 0, { row, offset -> row + offset }, { column, _ -> column })
    val diagonalRightDown = XmasStrategy(0, 3, 0, 3, { row, offset -> row + offset }, { column, offset -> column + offset })
    val diagonalLeftDown = XmasStrategy(0, 3, 3, 0, { row, offset -> row + offset }, { column, offset -> column - offset })
    val diagonalRightUp = XmasStrategy(3, 0, 0, 3, { row, offset -> row - offset }, { column, offset -> column + offset })
    val diagonalLeftUp = XmasStrategy(3, 0, 3, 0, { row, offset -> row - offset }, { column, offset -> column - offset })

    fun part1(input: List<String>): Int {
        val puzzle = input.map { it.toCharArray() }.toTypedArray()
        return listOf(
            puzzle.apply(right),
            puzzle.apply(left),
            puzzle.apply(up),
            puzzle.apply(down),
            puzzle.apply(diagonalRightDown),
            puzzle.apply(diagonalLeftDown),
            puzzle.apply(diagonalRightUp),
            puzzle.apply(diagonalLeftUp)
        ).sum()
    }

    fun Array<CharArray>.applyCrossMasMask(mask: CrossMasMask): Int {
        var count = 0
        for (row in 0 until this.size - 2) {
            for (column in 0 until this[row].size - 2) {
                if (this[row][column] == mask.upperLeft &&
                    this[row][column + 2] == mask.upperRight &&
                    this[row + 1][column + 1] == mask.middle &&
                    this[row + 2][column + 2] == mask.lowerRight &&
                    this[row + 2][column] == mask.lowerLeft) {
                    count++
                }
            }
        }
        return count
    }

    val crossMasMasks = listOf(
        CrossMasMask('M', 'M', 'A', 'S', 'S'),
        CrossMasMask('M', 'S', 'A', 'M', 'S'),
        CrossMasMask('S', 'S', 'A', 'M', 'M'),
        CrossMasMask('S', 'M', 'A', 'S', 'M')
    )
    fun part2(input: List<String>): Int {
        val puzzle = input.map { it.toCharArray() }.toTypedArray()
        return crossMasMasks.sumOf { puzzle.applyCrossMasMask(it) }
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 18)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 9)
    part2(input).println()
}

data class CrossMasMask(
    val upperLeft: Char,
    val upperRight: Char,
    val middle: Char,
    val lowerLeft: Char,
    val lowerRight: Char,
)

data class XmasStrategy(
    val rowMinOffset: Int,
    val rowMaxOffset: Int,
    val columnMinOffset: Int,
    val columnMaxOffset: Int,
    val rowOperation: (Int, Int) -> Int,
    val columnOperation: (Int, Int) -> Int
)
