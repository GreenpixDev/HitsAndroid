package ru.hits.android.axolot.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.hits.android.axolot.R
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.util.convertDpToPixel
import ru.hits.android.axolot.util.getThemeColor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Вьюшка для отрисовки линии (ребра), которая будет соединять пины
 */
class EdgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : View(context, attrs, defstyleAttr, defstyleRes) {

    companion object {
        /**
         * Количество промежуточных точек на прямой.
         * Меньше - сплайн более кривой.
         * Больше сплайн лучше, но менее производительный.
         */
        const val POINT_COUNT = 10

        /**
         * Растояние вспомогательных точек
         */
        const val BUFFER_POINT_DISTANCE = 75f

        /**
         * Нужно ли рисовать вспомогательные точки
         */
        const val BUFFER_POINT_DRAW_ENABLED = true

        /**
         * Радиус точки в DPI
         */
        const val CIRCLE_RADIUS_DP = 6.25f
    }

    private var paintBrush: Paint = Paint()

    val points = mutableListOf<Vec2f>()

    init {
        paintBrush.color = context.getThemeColor(R.attr.colorLine)
        paintBrush.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val circleRadius = context.convertDpToPixel(CIRCLE_RADIUS_DP)
        val drawPoints = generateDrawPoints()

        for (i in 0 until drawPoints.size - 1) {
            canvas?.drawLine(
                drawPoints[i].x,
                drawPoints[i].y,
                drawPoints[i + 1].x,
                drawPoints[i + 1].y,
                paintBrush
            )
        }

        for (i in 0 until points.size) {
            canvas?.drawCircle(points[i].x, points[i].y, circleRadius, paintBrush)
        }
    }

    private fun generateDrawPoints(): MutableList<Vec2f> {
        val sourcePoints = points.toMutableList()

        if (BUFFER_POINT_DRAW_ENABLED) {
            val delta = abs(sourcePoints[sourcePoints.size - 1].x - sourcePoints[0].x) / 2
            val distance = min(max(0f, delta), BUFFER_POINT_DISTANCE)

            sourcePoints.add(
                1, Vec2f(
                    x = sourcePoints[0].x + distance,
                    y = sourcePoints[0].y
                )
            )
            sourcePoints.add(
                index = sourcePoints.size - 1,
                element = Vec2f(
                    x = sourcePoints[sourcePoints.size - 1].x - distance,
                    y = sourcePoints[sourcePoints.size - 1].y
                )
            )
        }

        val drawPoints = mutableListOf<Vec2f>()

        if (sourcePoints.size < 2) {
            for (i in 0 until sourcePoints.size) {
                drawPoints.add(sourcePoints[i])
            }
            return drawPoints
        }

        for (i in 0 until sourcePoints.size - 1) {
            drawPoints.add(sourcePoints[i])
            var t = 0f
            val b = 0f
            val c = 0f
            val currentPoint = sourcePoints[i]
            val nextPoint = sourcePoints[i + 1]
            var next2Point = sourcePoints[i + 1]
            var previosPoint = sourcePoints[i]
            if (i + 2 < sourcePoints.size) {
                next2Point = sourcePoints[i + 2]
            }
            if (i != 0) {
                previosPoint = sourcePoints[i - 1]
            }
            val dix =
                (1f - t) * (1f + b) * (1f + c) / 2f * (currentPoint.x - previosPoint.x) + (1f - t) * (1f - b) * (1f - c) / 2f * (nextPoint.x - currentPoint.x)
            val diy =
                (1f - t) * (1f + b) * (1f + c) / 2f * (currentPoint.y - previosPoint.y) + (1f - t) * (1f - b) * (1f - c) / 2f * (nextPoint.y - currentPoint.y)
            val dinextx =
                (1f - t) * (1f + b) * (1f - c) / 2f * (nextPoint.x - currentPoint.x) + (1f - t) * (1f - b) * (1f - c) / 2f * (next2Point.x - nextPoint.x)
            val dinexty =
                (1f - t) * (1f + b) * (1f - c) / 2f * (nextPoint.y - currentPoint.y) + (1f - t) * (1f - b) * (1f - c) / 2f * (next2Point.y - nextPoint.y)

            val step = 1f / POINT_COUNT
            t = 0f
            for (j in 0 until POINT_COUNT) {
                val x: Float =
                    dix * h3(t) + currentPoint.x * h1(t) + nextPoint.x * h2(t) + dinextx * h4(t);
                val y: Float =
                    diy * h3(t) + currentPoint.y * h1(t) + nextPoint.y * h2(t) + dinexty * h4(t);
                drawPoints.add(Vec2f(x, y))
                t += step
            }

        }
        drawPoints.add(sourcePoints[sourcePoints.size - 1])
        return drawPoints
    }

    private fun h1(t: Float): Float {
        return 2 * t * t * t - 3 * t * t + 1
    }

    private fun h2(t: Float): Float {
        return -2 * t * t * t + 3 * t * t;
    }

    private fun h3(t: Float): Float {
        return t * t * t - 2 * t * t + t;
    }

    private fun h4(t: Float): Float {
        return t * t * t - t * t;
    }
}