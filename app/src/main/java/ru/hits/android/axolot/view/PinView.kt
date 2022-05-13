package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.databinding.PinItemBinding

@SuppressLint("ViewConstructor")
class PinView @JvmOverloads constructor(
    val pin: Pin,
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : LinearLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = PinItemBinding.inflate(LayoutInflater.from(context), this)

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
}