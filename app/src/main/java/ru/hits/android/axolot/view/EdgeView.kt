package ru.hits.android.axolot.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
        const val BUFFER_POINT_DISTANCE = 200f

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
        val mPath = Path()

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

        mPath.reset();
        mPath.moveTo(points.first().x, points.first().y);
        mPath.cubicTo(
            sourcePoints[1].x,
            sourcePoints[1].y,
            sourcePoints[2].x,
            sourcePoints[2].y,
            sourcePoints[3].x,
            sourcePoints[3].y
        )
        paintBrush.setStyle(Paint.Style.STROKE)
        canvas?.drawPath(mPath, paintBrush)
        paintBrush.setStyle(Paint.Style.FILL)
        for (i in 0 until points.size) {
            canvas?.drawCircle(points[i].x, points[i].y, circleRadius, paintBrush)
        }

    }//Когда то тут были Kochanek–Bartels spline... F... Теперь лишь пустота...


}