package year2024.day15

import utils.map.CharMap
import utils.movement.Direction
import utils.point.Point
import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 15

    fun part1(input: List<String>): Int {
        val (warehouse, moves) = input.partition { it.isNotEmpty() && it[0] == '#' }.let { (w, m) ->
            CharMap.of(w) to m.drop(0).joinToString("").map { Direction.fromChar(it) }
        }
        val robot = Robot(warehouse.getLocations("@")[0], warehouse)
        moves.forEach {
            robot.move(it)
        }
        println(warehouse.printable())
        val boxes = warehouse.getLocations("O")
        val sum = boxes.sumOf { it.row * 100 + it.column }
        return sum
    }

    fun part2(input: List<String>): Int {
        val (warehouse, moves) = input.partition { it.isNotEmpty() && it[0] == '#' }.let { (w, m) ->
            val newWarehouse = w.map { it.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.") }
            CharMap.of(newWarehouse) to m.drop(0).joinToString("").map { Direction.fromChar(it) }
        }
        val boxes = warehouse.getLocations("\\[").map { Box(it) }
        val robot = Robot2(warehouse.getLocations("@")[0], warehouse, boxes)
        moves.forEach {
            robot.move(it)
        }
        println(warehouse.printable())
        val sum = boxes.sumOf { it.gps }
        return sum
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 10092)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 9021)
    part2(input).println()
}

data class Box(var position: Point) {

    private var boxEnd = position + Point(1, 0)

    fun nextPositionsToCheck(direction: Direction): List<Point> {
        return when (direction) {
            Direction.NORTH, Direction.SOUTH -> {
                listOf(position + direction.offset, boxEnd + direction.offset)
            }

            Direction.EAST -> {
                listOf(boxEnd + direction.offset)
            }

            Direction.WEST -> {
                listOf(position + direction.offset)
            }
        }
    }

    fun move(direction: Direction, warehouse: CharMap) {
        warehouse[position] = '.'
        warehouse[boxEnd] = '.'
        position += direction.offset
        boxEnd += direction.offset
        warehouse[position] = '['
        warehouse[boxEnd] = ']'
    }

    fun isThisBox(positions: List<Point>): Boolean {
        return positions.any { it == this.position || it == boxEnd }
    }

    val gps: Int
        get() = position.row * 100 + position.column
}

data class Robot2(var position: Point, val warehouse: CharMap, val boxes: List<Box>) {
    fun move(move: Direction) {
        val next = position + move.offset
        if (warehouse[next] !in "#[]]") {
            moveCharacter(next)
        } else if (warehouse[next] == '#') {
            // do nothing
        } else {
            val obstaclesMoved = moveObstacles(listOf(next), move)
            if (obstaclesMoved) {
                moveCharacter(next)
            }
        }
    }

    private fun moveObstacles(locations: List<Point>, move: Direction): Boolean {
        val curBoxes = boxes.filter { it.isThisBox(locations) }
        val nexts = curBoxes.map { it.nextPositionsToCheck(move) }.flatten()
        if (warehouse[nexts].any { it == '#' }) {
            return false
        }
        if (warehouse[nexts].all { it == '.' }) {
            curBoxes.forEach { it.move(move, warehouse) }
            return true
        }
        if (moveObstacles(nexts, move)) {
            curBoxes.forEach { it.move(move, warehouse) }
            return true
        }
        return false
    }

    private fun moveCharacter(next: Point) {
        warehouse[position] = '.'
        position = next
        warehouse[position] = '@'
    }
}

data class Robot(var position: Point, val warehouse: CharMap) {
    fun move(move: Direction) {
        val next = position + move.offset
        if (warehouse[next] !in "#O") {
            moveCharacter(next)
        } else if (warehouse[next] == '#') {
            // do nothing
        } else if (warehouse[next] == 'O') {
            val obstaclesMoved = moveObstacles(next, move)
            if (obstaclesMoved) {
                moveCharacter(next)
            }
        }
    }

    private fun moveCharacter(next: Point) {
        warehouse[position] = '.'
        position = next
        warehouse[position] = '@'
    }

    private fun moveObstacles(start: Point, move: Direction): Boolean {
        var obstacleCounter = 1
        var next = start + move.offset
        while (warehouse[next] == 'O') {
            obstacleCounter++
            next += move.offset
        }
        if (warehouse[next] == '#') {
            return false
        }
        while (obstacleCounter > 0) {
            warehouse[next] = 'O'
            next -= move.offset
            obstacleCounter--
        }
        return true
    }
}
