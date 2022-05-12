package ru.hits.android.axolot.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.hits.android.axolot.util.Vec2f

class LineCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : View(context, attrs, defstyleAttr, defstyleRes) {

    companion object {
        const val POINTSCOUNT =
            50 // количество промежуточных точек на прямой (меньше - сплайн более кривой), больше сплайн лучше, но нагружает пк.
    }

    var mPaint: Paint = Paint();

    val points = mutableListOf<Vec2f>()

    init {
        mPaint.setColor(Color.BLUE)
        mPaint.setStrokeWidth(3f)
        points.add(Vec2f(0f, 0f))
        points.add(Vec2f(10f, 0f))
        points.add(Vec2f(200f, 0f))
        points.add(Vec2f(200f, 100f))
        points.add(Vec2f(500f, 100f))
    }

    fun h1(t: Float): Float {
        return 2 * t * t * t - 3 * t * t + 1
    }

    fun h2(t: Float): Float {
        return -2 * t * t * t + 3 * t * t;
    }

    fun h3(t: Float): Float {
        return t * t * t - 2 * t * t + t;
    }

    fun h4(t: Float): Float {
        return t * t * t - t * t;
    }

    fun generateDrawPoints(): MutableList<Vec2f> {
        val drawPoints = mutableListOf<Vec2f>()
        if (points.size < 2) {
            for (i in 0 until points.size) {
                drawPoints.add(points[i])
            }
            return drawPoints
        }

        for (i in 0 until points.size - 1) {
            var t = 0f
            val b = 0f
            val c = 0f
            val currentPoint = points[i]
            val nextPoint = points[i + 1]
            var next2Point = points[i + 1]
            var previosPoint = points[i]
            if (i + 2 < points.size) {
                next2Point = points[i + 2]
            }
            if (i != 0) {
                previosPoint = points[i - 1]
            }
            var dix =
                (1f - t) * (1f + b) * (1f + c) / 2f * (currentPoint.x - previosPoint.x) + (1f - t) * (1f - b) * (1f - c) / 2f * (nextPoint.x - currentPoint.x)
            var diy =
                (1f - t) * (1f + b) * (1f + c) / 2f * (currentPoint.y - previosPoint.y) + (1f - t) * (1f - b) * (1f - c) / 2f * (nextPoint.y - currentPoint.y)
            var dinextx =
                (1f - t) * (1f + b) * (1f - c) / 2f * (nextPoint.x - currentPoint.x) + (1f - t) * (1f - b) * (1f - c) / 2f * (next2Point.x - nextPoint.x)
            var dinexty =
                (1f - t) * (1f + b) * (1f - c) / 2f * (nextPoint.y - currentPoint.y) + (1f - t) * (1f - b) * (1f - c) / 2f * (next2Point.y - nextPoint.y)

            var step = 1f / POINTSCOUNT
            t = 0f
            for (j in 0 until POINTSCOUNT) {
                var x: Float
                var y: Float
                x = dix * h3(t) + currentPoint.x * h1(t) + nextPoint.x * h2(t) + dinextx * h4(t);
                y = diy * h3(t) + currentPoint.y * h1(t) + nextPoint.y * h2(t) + dinexty * h4(t);
                drawPoints.add(Vec2f(x, y))
                t += step
            }
        }
        return drawPoints
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val drawPoints = generateDrawPoints()

        for (i in 0 until drawPoints.size - 1) {
            canvas?.drawLine(
                drawPoints[i].x,
                drawPoints[i].y,
                drawPoints[i + 1].x,
                drawPoints[i + 1].y,
                mPaint
            )
        }

        for (i in 0 until points.size) {
            canvas?.drawCircle(points[i].x, points[i].y, 10f, mPaint)
        }
    }

}