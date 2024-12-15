package utils.point

import kotlin.math.absoluteValue

@Suppress("unused")
data class Point(val x: Int, val y: Int) {
    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())

    fun left() = Point(x - 1, y)
    fun right() = Point(x + 1, y)
    fun up() = Point(x, y - 1)
    fun down() = Point(x, y + 1)

    val column: Int
        get() = x
    val width: Int
        get() = x

    val row: Int
        get() = y
    val height: Int
        get() = y

    fun isInside(maxX: Int, maxY: Int) = x in 0 until maxX && y in 0 until maxY

    fun scalarFit(other: Point): Int {
        val columnFit = other.x / x
        val rowFit = other.y / y
        return minOf(columnFit, rowFit)
    }


    fun adjacent(point: Point): Boolean {
        return manhattanDistance(point) == 1
    }

    fun manhattanDistance(point: Point): Int {
        return (x - point.x).absoluteValue + (y - point.y).absoluteValue
    }

    fun wrap(p: Point): Point {
        val x = (x + p.width) % p.width
        val y = (y + p.height) % p.height
        return Point(x, y)
    }

    operator fun times(scalar: Int) = Point(x * scalar, y * scalar)
    operator fun div(scalar: Int) = Point(x / scalar, y / scalar)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun plus(scalar: Int) = Point(x + scalar, y + scalar)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun hashCode(): Int {
        return x * 10000 + y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}
