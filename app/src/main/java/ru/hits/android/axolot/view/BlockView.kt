package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.pin_item.view.*
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.databinding.BlockItemBinding
import ru.hits.android.axolot.util.Vec2f

class BlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : ConstraintLayout(context, attrs, defstyleAttr, defstyleRes), BlueprintView {

    private val binding = BlockItemBinding.inflate(LayoutInflater.from(context), this)

    private var offset = Vec2f.ZERO

    private val _pinViews = mutableListOf<PinView>()
    val pinViews: List<PinView>
        get() = _pinViews

    lateinit var block: AxolotBlock

    /**
     * Метод добавления вьюшки пина (или узла/булавочки/круглешочка)
     */
    @SuppressLint("ClickableViewAccessibility")
    fun createPinView(pin: Pin, indexGetter: (Int) -> Int = { it }): PinView {
        val pinView = PinView(context)

        pinView.pin = pin
        pinView.description.text = pin.name

        pinView.addViewTo(this, indexGetter)
        _pinViews.add(pinView)
        return pinView
    }

    /**
     * Метод добавление вьюшки кнопочки "Add Pin"
     */
    fun createAddPinView(declaredPin: DeclaredPin) {
        when (declaredPin) {
            is DeclaredVarargInputDataPin -> {
                val addNodeView = AddNodeView(context)
                addNodeView.initComponents()
                addNodeView.setOnClickListener { _ ->
                    declaredPin.createPin(block).forEach { pin ->
                        block.contacts.add(pin)
                        createPinView(pin) { it - 1 }
                    }
                }
                binding.body.linearLayoutLeft.addView(addNodeView)
            }
            is DeclaredVarargOutputFlowPin -> {
                val addNodeView = AddNodeView(context)
                addNodeView.initComponents()
                addNodeView.setOnClickListener { _ ->
                    declaredPin.createPin(block).forEach { pin ->
                        block.contacts.add(pin)
                        createPinView(pin) { it - 1 }
                    }
                }
                binding.body.linearLayoutRight.addView(addNodeView)
            }
        }
    }

    /**
     * Метод передвижения вьюшек с учетом зума
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val zoom = zoom.realZoom
        val pan = Vec2f(this.zoom.panX, this.zoom.panY) * -1

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                offset = (Vec2f(x, y) - pan) * zoom - Vec2f(event.rawX, event.rawY)
            }
            MotionEvent.ACTION_MOVE -> {

                x = (event.rawX + offset.x) / zoom + pan.x
                y = (event.rawY + offset.y) / zoom + pan.y
            }
        }
        return true
    }
}