package utils.point

data class Rectangle(val x0: Int, val y0: Int, val x1: Int, val y1: Int) {
    constructor(p0: Point, p1: Point) : this(p0.x, p0.y, p1.x, p1.y)

    operator fun contains(vector: Point): Boolean {
        return vector.x in x0 until x1 && vector.y in y0 until y1
    }
}
