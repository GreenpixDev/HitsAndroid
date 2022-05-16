package ru.hits.android.axolot.util

/**
 * Интерфейс двумерного вектора
 */
interface Vec2 {

    val x: Number

    val y: Number

    operator fun times(scalar: Number): Vec2

    operator fun div(scalar: Number): Vec2

    operator fun plus(vec: Vec2): Vec2

    operator fun minus(vec: Vec2): Vec2

    operator fun unaryMinus(): Vec2

}

/**
 * Двумерный вектор с типом данных [Float]
 */
data class Vec2f(override val x: Float, override val y: Float) : Vec2 {

    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())

    companion object {

        val ZERO = Vec2f(0f, 0f)

    }

    override operator fun times(scalar: Number) = Vec2f(x * scalar.toFloat(), y * scalar.toFloat())

    override operator fun div(scalar: Number) = Vec2f(x / scalar.toFloat(), y / scalar.toFloat())

    override operator fun plus(vec: Vec2) = Vec2f(x + vec.x.toFloat(), y + vec.y.toFloat())

    override operator fun minus(vec: Vec2) = Vec2f(x - vec.x.toFloat(), y - vec.y.toFloat())

    override operator fun unaryMinus() = this * -1f

    override fun toString() = "Vec2f(x=$x, y=$y)"

}

/**
 * Двумерный вектор с типом данных [Double]
 */
data class Vec2d(override val x: Double, override val y: Double) : Vec2 {

    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

    companion object {

        val ZERO = Vec2d(0.0, 0.0)

    }

    override operator fun times(scalar: Number) =
        Vec2d(x * scalar.toDouble(), y * scalar.toDouble())

    override operator fun div(scalar: Number) = Vec2d(x / scalar.toDouble(), y / scalar.toDouble())

    override operator fun plus(vec: Vec2) = Vec2d(x + vec.x.toDouble(), y + vec.y.toDouble())

    override operator fun minus(vec: Vec2) = Vec2d(x - vec.x.toDouble(), y - vec.y.toDouble())

    override operator fun unaryMinus() = this * -1f

    override fun toString() = "Vec2d(x=$x, y=$y)"

}

/**
 * Двумерный вектор с типом данных [Int]
 */
data class Vec2i(override val x: Int, override val y: Int) : Vec2 {

    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())

    companion object {

        val ZERO = Vec2i(0, 0)

    }

    override operator fun times(scalar: Number) = Vec2i(x * scalar.toInt(), y * scalar.toInt())

    override operator fun div(scalar: Number) = Vec2i(x / scalar.toInt(), y / scalar.toInt())

    override operator fun plus(vec: Vec2) = Vec2i(x + vec.x.toInt(), y + vec.y.toInt())

    override operator fun minus(vec: Vec2) = Vec2i(x - vec.x.toInt(), y - vec.y.toInt())

    override operator fun unaryMinus() = this * -1f

    override fun toString() = "Vec2i(x=$x, y=$y)"

}

/**
 * Перевести любой двумерный вектор в [Vec2f]
 */
fun Vec2.toFloat(): Vec2f {
    return Vec2f(x, y)
}

/**
 * Перевести любой двумерный вектор в [Vec2d]
 */
fun Vec2.toDouble(): Vec2d {
    return Vec2d(x, y)
}

/**
 * Перевести любой двумерный вектор в [Vec2i]
 */
fun Vec2.toInt(): Vec2i {
    return Vec2i(x, y)
}