package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.otaliastudios.zoom.ZoomLayout
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.databinding.PinItemBinding
import ru.hits.android.axolot.util.Vec2f

class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : LinearLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = PinItemBinding.inflate(LayoutInflater.from(context), this)

    private var offset = Vec2f.ZERO

    private var shadowPinView: ImageView? = null

    lateinit var pin: Pin

    lateinit var zoomLayout: ZoomLayout

    lateinit var codeFieldView: ViewGroup

    init {
        //binding.contact.setOnDragListener(this::onDragEvent)
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
        val zoom = zoomLayout.realZoom
        val pan = Vec2f(zoomLayout.panX, zoomLayout.panY) * -1

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                shadowPinView = ImageView(context)
                shadowPinView?.layoutParams = LayoutParams(view.width, view.height)
                shadowPinView?.background = view.background
                shadowPinView?.x = event.rawX / zoom + pan.x
                shadowPinView?.y = event.rawY / zoom + pan.y
                codeFieldView.addView(shadowPinView)
            }
            MotionEvent.ACTION_MOVE -> {
                shadowPinView?.x = event.rawX / zoom + pan.x
                shadowPinView?.y = event.rawY / zoom + pan.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                codeFieldView.removeView(shadowPinView)
                shadowPinView = null
            }
        }
        return true
    }
}