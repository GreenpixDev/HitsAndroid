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

@SuppressLint("ClickableViewAccessibility")
class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : LinearLayout(context, attrs, defstyleAttr, defstyleRes), BlueprintView {

    private val binding = PinItemBinding.inflate(LayoutInflater.from(context), this)

    private var lineCanvas: LineCanvasView? = null

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
            else -> throw IllegalArgumentException("fatal error") // TODO нормальное сообщение у ошибки сделать
        }
        layout.addView(this, indexGetter.invoke(layout.childCount))
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

                lineCanvas = LineCanvasView(context)
                lineCanvas?.inverse = pin is InputPin
                lineCanvas?.position = center

                lineCanvas?.points?.add(Vec2f.ZERO)
                lineCanvas?.points?.add(Vec2f.ZERO)

                activity.codeField.addView(lineCanvas)
            }

            // Когда двигаем пальцем - отрисовываем каждый кадр
            MotionEvent.ACTION_MOVE -> {
                lineCanvas?.let {
                    it.points[it.points.size - 1] = pointer - it.position
                    it.invalidate()
                }
            }

            // Когда отжимаем палец - либо удаляем линию, либо сохраняем её и соединяем пины
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val parent =
                    activity.codeField.findViewAt(pointer) { it !is LineCanvasView }?.parent

                if (parent != null && parent is PinView && parent != this) {
                    val position = activity.codeField.findRelativePosition(parent.contact)
                    val center = position + view.center

                    lineCanvas?.let {
                        it.points[it.points.size - 1] = center - it.position
                        it.invalidate()
                    }

                    // TODO соединить пины в промежуточном слое
                } else {
                    activity.codeField.removeView(lineCanvas)
                }
                lineCanvas = null
            }
        }
        return true
    }
}