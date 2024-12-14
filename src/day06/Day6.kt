package day06

import utils.println
import utils.readInput

fun main() {
    val latestDay = 6

    fun initializeState(input: List<String>): Guard {
        val map = input.map { it.toCharArray() }.toTypedArray()
        val guardPosition = map.withIndex().find { (_, row) -> row.contains('^') }!!.let { (rowIndex, row) ->
            rowIndex to row.indexOf('^')
        }.toMutablePair()
        val direction = Direction6.fromChar(map[guardPosition.row][guardPosition.column])
        val guard = Guard(guardPosition, direction, map)
        return guard
    }

    fun part1(input: List<String>): Int {
        val guard = initializeState(input)
        try {
            while(true) {
                if (guard.isBlocked()) {
                    guard.blocked()
                } else {
                    guard.move()
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            guard.markVisited()
            println("Reached end of map")
        }
        return guard.sumVisits()
    }

    fun part2(input: List<String>): Int {
        val guard = initializeState(input)
        guard.saveInitialGuardState()
        var sumPositions = 0
        for (row in 0..<guard.map.size) {
            for (column in 0..<guard.map[0].size) {
                guard.resetToInitialGuardState()
                if (guard.map[row][column] != '.')
                    continue
                else
                    guard.map[row][column] = 'O'
                try {
                    while (true) {
                        when {
                            guard.isStuckInLoop() -> {
                                sumPositions++
                                break
                            }
                            guard.isBlocked() || guard.isBlockedByBlockage() -> {
                                guard.blocked()
                            }
                            else -> {
                                guard.move()
                            }
                        }
                    }
                } catch (_: IndexOutOfBoundsException) {
                }

            }
        }

        return sumPositions
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 41)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 6)
    part2(input).println()
}

enum class Direction6(val value: Int) {
    UP(0), RIGHT(1), DOWN(2), LEFT(3);

    fun turnRight() = when(this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    companion object {
        fun fromChar(char: Char) = when(char) {
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            '<' -> LEFT
            else -> throw IllegalArgumentException("Invalid direction character: $char")
        }
    }
}

data class Guard(val position: MutablePair, var direction: Direction6, var map: Array<CharArray>) {
    private var initialGuardState: Guard? = null
    private var historyHashes = mutableSetOf<Int>()

    fun move() {
        markVisited()
        when(direction) {
            Direction6.UP -> position.row--
            Direction6.DOWN -> position.row++
            Direction6.LEFT -> position.column--
            Direction6.RIGHT -> position.column++
        }
    }

    private fun returnNextPosition() = when(direction) {
        Direction6.UP -> map[position.row - 1][position.column]
        Direction6.DOWN -> map[position.row + 1][position.column]
        Direction6.LEFT -> map[position.row][position.column - 1]
        Direction6.RIGHT -> map[position.row][position.column + 1]
    }

    fun isBlocked() = when {
        returnNextPosition() == '#' -> true
        else -> false
    }

    fun isBlockedByBlockage() = when {
        returnNextPosition() == 'O' -> true
        else -> false
    }

    fun isStuckInLoop(): Boolean {
        val currentPositionHash = position.hashCode() * 10 + direction.value
        if (currentPositionHash in historyHashes)
            return true
        else
            historyHashes.add(currentPositionHash)
        return false
    }

    fun blocked() {
        direction = direction.turnRight()
    }

    fun markVisited() {
        map.put('X', inFront = false)
    }

    fun saveInitialGuardState() {
        initialGuardState = this.copy(position = position.copy(), direction = direction, map = map.copy()).also { it.move() }
    }

    fun resetToInitialGuardState() {
        position.row = initialGuardState!!.position.row
        position.column = initialGuardState!!.position.column
        direction = initialGuardState!!.direction
        map = initialGuardState!!.map.copy()
        historyHashes = mutableSetOf()
    }

    private fun Array<CharArray>.put(char: Char, inFront: Boolean) {
        if(inFront) {
            when(direction) {
                Direction6.UP -> this[position.row - 1][position.column] = char
                Direction6.DOWN -> this[position.row + 1][position.column] = char
                Direction6.LEFT -> this[position.row][position.column - 1] = char
                Direction6.RIGHT -> this[position.row][position.column + 1] = char
            }
        } else {
            this[position.row][position.column] = char
        }
    }

    fun sumVisits() =
        map.sumOf { it.count { cell -> cell == 'X' } }
}

data class MutablePair(var row: Int, var column: Int) {
    override fun hashCode(): Int {
        return row * 1000 + column
    }
}

fun Pair<Int, Int>.toMutablePair() = MutablePair(first, second)

fun Array<CharArray>.copy() = Array(size) { get(it).clone() }
