package ru.hits.android.axolot.util

interface Vec2 {

    val x: Number

    val y: Number

    operator fun times(scalar: Number): Vec2

    operator fun div(scalar: Number): Vec2

    operator fun plus(vec: Vec2): Vec2

    operator fun minus(vec: Vec2): Vec2

    operator fun unaryMinus(): Vec2

}

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