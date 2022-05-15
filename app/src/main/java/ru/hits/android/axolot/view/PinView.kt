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
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.util.findViewAt
import ru.hits.android.axolot.util.getPositionRelative

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

    private fun onTouchEvent(view: View, event: MotionEvent): Boolean {
        val zoom = zoom.realZoom
        val pan = Vec2f(this.zoom.panX, this.zoom.panY) * -1

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val position = view.getPositionRelative(activity.zoomLayout)
                val center = position + Vec2f(view.width, view.height) / 2
                lineCanvas = LineCanvasView(context)
                lineCanvas?.inverse = pin is InputPin
                lineCanvas?.x = center.x
                lineCanvas?.y = center.y

                lineCanvas?.points?.add(Vec2f.ZERO)
                lineCanvas?.points?.add(Vec2f.ZERO)

                activity.codeField.addView(lineCanvas)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointer = Vec2f(event.rawX / zoom + pan.x, event.rawY / zoom + pan.y)
                lineCanvas?.let {
                    it.points[it.points.size - 1] = pointer - Vec2f(it.x, it.y)
                    it.invalidate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val pointer = Vec2f(event.rawX / zoom + pan.x, event.rawY / zoom + pan.y)
                val parent = activity.codeField.findViewAt(pointer)?.parent

                if (parent != null && parent is PinView && parent != this) {
                    val position = parent.contact.getPositionRelative(activity.zoomLayout)
                    val center = position + Vec2f(view.width, view.height) / 2
                    lineCanvas?.let {
                        it.points[it.points.size - 1] = center - Vec2f(it.x, it.y)
                        it.invalidate()
                    }
                } else {
                    activity.codeField.removeView(lineCanvas)
                }

                lineCanvas = null
            }
        }
        return true
    }
}