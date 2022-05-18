package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.databinding.BlockItemBinding
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.util.findRelativePosition
import ru.hits.android.axolot.util.position

/**
 * Вьюшка для отображения блока на поле
 */
class BlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : ConstraintLayout(context, attrs, defstyleAttr, defstyleRes), BlueprintView {

    private val binding = BlockItemBinding.inflate(LayoutInflater.from(context), this)

    private val _pinViews = mutableListOf<PinView>()

    private var offset = Vec2f.ZERO

    lateinit var block: AxolotBlock

    val pinViews: List<PinView>
        get() = _pinViews

    var position: Vec2f
        get() = Vec2f(x, y)
        set(value) {
            x = value.x
            y = value.y
            block.position = value
        }

    /**
     * Отображаемое название блока в заголовке
     */
    var displayName: String
        get() = binding.title.text.toString()
        set(value) {
            binding.title.text = value
        }

    /**
     * Метод добавления вьюшки пина (или узла/булавочки/круглешочка)
     */
    @SuppressLint("ClickableViewAccessibility")
    fun createPinView(pin: Pin, indexGetter: (Int) -> Int = { it }): PinView {
        val pinView = PinView(context)

        pinView.pin = pin
        pinView.displayName = pin.name
        pinView.update()

        _pinViews.add(pinView)
        pinView.addViewTo(this, indexGetter)
        return pinView
    }

    /**
     * Метод добавление вьюшки кнопочки "Add Pin"
     */
    fun createAddPinView(declaredPin: DeclaredPin) {
        when (declaredPin) {
            // Если это входной пин
            is DeclaredVarargInputDataPin -> {
                //параметры для add pin
                val params = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 15
                }

                val addNodeView = AddNodeView(context)
                addNodeView.layoutParams = params
                addNodeView.initComponents()
                addNodeView.setOnClickListener { _ ->
                    declaredPin.createOnePin(block).let { pin ->
                        block.contacts.add(pin)
                        createPinView(pin) { it - 1 }
                    }
                }
                binding.body.linearLayoutLeft.addView(addNodeView)
            }

            // Если это выходной пин
            is DeclaredVarargOutputFlowPin -> {
                val addNodeView = AddNodeView(context)
                addNodeView.initComponents()
                addNodeView.setOnClickListener { _ ->
                    declaredPin.createOnePin(block).let { pin ->
                        block.contacts.add(pin)
                        createPinView(pin) { it - 1 }
                    }
                }
                binding.body.linearLayoutRight.addView(addNodeView)
            }
        }
    }

    /**
     * Метод передвижения вьюшки относительно поля
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointer = activity.codeField.findRelativePosition(this) + event.position

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                offset = event.position
            }
            MotionEvent.ACTION_MOVE -> {
                val to = pointer - offset
                val delta = to - position
                position = to

                _pinViews.forEach { it.move(delta) }
            }
        }
        return true
    }
}