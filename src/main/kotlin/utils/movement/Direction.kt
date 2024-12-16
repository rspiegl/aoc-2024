package utils.movement

import utils.point.Point

@Suppress("unused")
enum class Direction(val offset: Point) {
    NORTH(Point(0, -1)),
    EAST(Point(1, 0)),
    SOUTH(Point(0, 1)),
    WEST(Point(-1, 0));

    fun turnRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turnLeft() = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }

    fun toChar(): Char = when (this) {
        NORTH -> '^'
        EAST -> '>'
        SOUTH -> 'v'
        WEST -> '<'
    }

    companion object {
        fun fromChar(c: Char) = when (c.lowercaseChar()) {
            'n' -> NORTH
            'e' -> EAST
            's' -> SOUTH
            'w' -> WEST
            '^' -> NORTH
            '>' -> EAST
            'v' -> SOUTH
            '<' -> WEST
            else -> throw IllegalArgumentException("Invalid direction: $c")
        }

        fun fromPoints(from: Point, to: Point): Direction {
            val diff = to - from
            return when {
                diff.x < 0 -> WEST
                diff.x > 0 -> EAST
                diff.y < 0 -> NORTH
                diff.y > 0 -> SOUTH
                else -> throw IllegalArgumentException("Invalid direction: $from -> $to")
            }
        }
    }
}
