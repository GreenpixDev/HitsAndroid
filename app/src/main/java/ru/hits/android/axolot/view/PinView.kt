package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.pin_item.view.*
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.databinding.PinItemBinding
import ru.hits.android.axolot.util.*

/**
 * Вьюшка для отображения пина (или узла/булавочки/круглешочка) у блока
 */
@SuppressLint("ClickableViewAccessibility")
class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : LinearLayout(context, attrs, defstyleAttr, defstyleRes), BlueprintView {

    private val binding = PinItemBinding.inflate(LayoutInflater.from(context), this)

    private val edgeViews = mutableListOf<EdgeView>()

    lateinit var pin: Pin

    init {
        binding.contact.setOnTouchListener(this::onTouchEvent)
    }

    /**
     * Добавить вьюшку этого пина к [parentView], причем индекс вставки этой вьюшки
     * задается через функцию высшего порядка [indexGetter].
     * Таким образом, можно вставлять эту вьюшку в любое место в списке
     */
    fun addViewTo(parentView: BlockView, indexGetter: (Int) -> Int) {
        val layout = when (pin) {
            is InputPin -> {
                layoutDirection = LAYOUT_DIRECTION_LTR
                parentView.body.linearLayoutLeft
            }
            is OutputPin -> {
                layoutDirection = LAYOUT_DIRECTION_RTL
                parentView.body.linearLayoutRight
            }
            else -> throw IllegalArgumentException("Pin must be InputPin or OutputPin")
        }
        layout.addView(this, indexGetter.invoke(layout.childCount))
    }

    /**
     * Сдвинуть конечную точку на ребре на [delta] относительно прошлой позиции
     */
    fun move(delta: Vec2f) {
        edgeViews.forEach {
            when (pin) {
                is InputPin -> it.points[it.points.size - 1] += delta
                is OutputPin -> it.points[0] += delta
                else -> throw IllegalArgumentException("Pin must be InputPin or OutputPin")
            }
            it.invalidate()
        }
    }

    /**
     * Установить конечную точку на ребре в [position] относительно поля
     */
    private fun EdgeView.setEndPoint(position: Vec2f) {
        when (pin) {
            is InputPin -> points[0] = position - this@setEndPoint.position
            is OutputPin -> points[points.size - 1] = position - this@setEndPoint.position
            else -> throw IllegalArgumentException("Pin must be InputPin or OutputPin")
        }
    }

    /**
     * Метод передвижения пинов, которые образают линию, относительно поля.
     * В результате работы этого метода можно соединить пины линией.
     */
    private fun onTouchEvent(view: View, event: MotionEvent): Boolean {
        val pointer = activity.codeField.findRelativePosition(view) + event.position

        when (event.action) {
            // Когда зажимаем палец - создаем линию между пинами для отрисовки
            MotionEvent.ACTION_DOWN -> {
                val position = activity.codeField.findRelativePosition(view)
                val center = position + view.center
                val edgeView = EdgeView(context)

                edgeView.position = center

                edgeView.points.add(Vec2f.ZERO)
                edgeView.points.add(Vec2f.ZERO)

                edgeViews.add(edgeView)
                activity.codeField.addView(edgeView)
            }

            // Когда двигаем пальцем - отрисовываем каждый кадр
            MotionEvent.ACTION_MOVE -> {
                val edgeView = edgeViews.last()
                edgeView.setEndPoint(pointer)
                edgeView.invalidate()
            }

            // Когда отжимаем палец - либо удаляем линию, либо сохраняем её и соединяем пины
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val edgeView = edgeViews.last()
                val parent =
                    activity.codeField.findViewAt(pointer) { it !is EdgeView }?.parent

                if (parent != null && parent is PinView && parent != this) {
                    val position = activity.codeField.findRelativePosition(parent.contact)
                    val center = position + view.center

                    edgeView.setEndPoint(center)
                    edgeView.invalidate()

                    parent.edgeViews.add(edgeView)
                    // TODO соединить пины в промежуточном слое
                } else {
                    edgeViews.removeLast()
                    activity.codeField.removeView(edgeView)
                }
            }
        }
        return true
    }
}