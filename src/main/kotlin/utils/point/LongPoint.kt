package utils.point

import kotlin.math.absoluteValue

@Suppress("unused")
data class LongPoint(val x: Long, val y: Long) {
    constructor(x: Number, y: Number) : this(x.toLong(), y.toLong())

    fun left() = LongPoint(x - 1, y)
    fun right() = LongPoint(x + 1, y)
    fun up() = LongPoint(x, y - 1)
    fun down() = LongPoint(x, y + 1)

    val column: Long
        get() = x

    val row: Long
        get() = y


    fun isInside(maxX: Long, maxY: Long) = x in 0 until maxX && y in 0 until maxY

    fun scalarFit(other: LongPoint): Long {
        val columnFit = other.x / x
        val rowFit = other.y / y
        return minOf(columnFit, rowFit)
    }

    fun adjacent(point: LongPoint): Boolean {
        return manhattanDistance(point) == 1L
    }

    fun manhattanDistance(point: LongPoint): Long {
        return (x - point.x).absoluteValue + (y - point.y).absoluteValue
    }

    fun wrap(p: Point): LongPoint {
        val x = (x + p.width) % p.width
        val y = (y + p.height) % p.height
        return LongPoint(x, y)
    }

    operator fun times(scalar: Long) = LongPoint(x * scalar, y * scalar)
    operator fun div(scalar: Long) = LongPoint(x / scalar, y / scalar)
    operator fun times(scalar: Int) = LongPoint(x * scalar, y * scalar)
    operator fun div(scalar: Int) = LongPoint(x / scalar, y / scalar)
    operator fun plus(other: LongPoint) = LongPoint(x + other.x, y + other.y)
    operator fun minus(other: LongPoint) = LongPoint(x - other.x, y - other.y)

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun hashCode(): Int {
        return (x * 31 + y).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LongPoint

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}
