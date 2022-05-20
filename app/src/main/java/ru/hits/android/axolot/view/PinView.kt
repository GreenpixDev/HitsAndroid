package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.InputType.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.pin_item.view.*
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredDataPin
import ru.hits.android.axolot.blueprint.element.pin.*
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.databinding.PinItemBinding
import ru.hits.android.axolot.exception.AxolotPinException
import ru.hits.android.axolot.exception.AxolotPinOneAdjacentException
import ru.hits.android.axolot.interpreter.type.Type
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

    private val _edgeViews = mutableListOf<EdgeView>()

    lateinit var pin: Pin

    val edgeViews: List<EdgeView>
        get() = _edgeViews

    init {
        binding.contact.setOnTouchListener(this::onTouchEvent)
    }

    /**
     * Получить цвет узла
     */
    private val color: Int
        get() {
            if (pin is FlowPin) {
                return context.getThemeColor(R.attr.colorFlowControl)
            }
            if (pin is TypedPin) {
                val pinType = (pin as TypedPin).type
                if (pinType is DeclaredDataPin) {
                    val colorName = "colorVariable${pinType.type}"
                    return context.getThemeColor(colorName)
                }
            }
            return Color.WHITE
        }

    /**
     * Отображаемое название пина
     */
    var displayName: String
        get() = binding.description.text.toString()
        set(value) {
            binding.description.text = value
        }

    /**
     * Обновить пин
     */
    fun update() {

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
        processInputField()
    }

    /**
     * В разметке указано, что все поля ввода и картинки для bool имеют тип gone.
     * То есть они свернуты, не занимают места в разметке.
     * Для пина конкретного типа мы сделаем видимым нужное поле, а после того, как
     * это поле станет ненужным (произойдет соединение этого пина), мы сделаем его
     * invisible (он станет невидимым, но продолжит занимать место)
     */
    private fun processInputField() {

        val currentPin = pin

        if (currentPin is InputDataPin) {

            when ((currentPin.type as DeclaredDataPin).type) {
                Type.INT -> {
                    binding.inputField.visibility = View.VISIBLE
                    binding.inputField.inputType = TYPE_CLASS_NUMBER + TYPE_NUMBER_FLAG_SIGNED
                    binding.inputField.setText("0")

                    binding.inputField.addTextChangedListener { inputDataBlock, _, _, _ ->
                        val inputData = inputDataBlock.toString()

                        //Если поле ввода пустое, то будем отправлять значения по умолчанию
                        if (inputData != "") {
                            activity.currentSource.setValue(currentPin, Type.INT, inputData.toInt())
                        } else {
                            activity.currentSource.setValue(currentPin, Type.INT, 0)
                        }
                    }
                }

                Type.FLOAT -> {
                    binding.inputField.visibility = View.VISIBLE
                    binding.inputField.inputType =
                        TYPE_CLASS_NUMBER + TYPE_NUMBER_FLAG_DECIMAL + TYPE_NUMBER_FLAG_SIGNED
                    binding.inputField.setText("0.0")

                    binding.inputField.addTextChangedListener { inputDataBlock, _, _, _ ->
                        val inputData = inputDataBlock.toString()

                        //Если поле ввода пустое, то будем отправлять значения по умолчанию
                        if (inputData != "") {
                            activity.currentSource.setValue(
                                currentPin,
                                Type.FLOAT,
                                inputData.toDouble()
                            )
                        } else {
                            activity.currentSource.setValue(currentPin, Type.FLOAT, 0.0)
                        }

                    }
                }

                Type.BOOLEAN -> {
                    binding.crossIcon.visibility = VISIBLE

                    binding.crossIcon.setOnClickListener {
                        if (binding.crossIcon.visibility == View.VISIBLE) {
                            binding.crossIcon.visibility = GONE
                            binding.tickIcon.visibility = VISIBLE
                        }

                        activity.currentSource.setValue(currentPin, Type.BOOLEAN, true)
                    }

                    binding.tickIcon.setOnClickListener {
                        if (binding.tickIcon.visibility == View.VISIBLE) {
                            binding.tickIcon.visibility = GONE
                            binding.crossIcon.visibility = VISIBLE

                            activity.currentSource.setValue(currentPin, Type.BOOLEAN, false)
                        }
                    }
                }

                Type.STRING -> {
                    binding.inputField.inputType = TYPE_CLASS_TEXT
                    binding.inputField.visibility = VISIBLE

                    binding.inputField.addTextChangedListener { inputDataBlock, _, _, _ ->
                        val inputData = inputDataBlock.toString()

                        activity.currentSource.setValue(currentPin, Type.STRING, inputData)
                    }
                }

                //если не надо для интерпретатора - убрать
                else -> {
                    binding.inputField.inputType = TYPE_NULL
                }
            }

        }
    }

    /**
     * Сдвинуть конечную точку на ребре на [delta] относительно прошлой позиции
     */
    fun move(delta: Vec2f) {
        _edgeViews.forEach {
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
                edgeView.paintBrush.color = color

                edgeView.points.add(Vec2f.ZERO)
                edgeView.points.add(Vec2f.ZERO)

                _edgeViews.add(edgeView)
                activity.codeField.addView(edgeView)
            }

            // Когда двигаем пальцем - отрисовываем каждый кадр
            MotionEvent.ACTION_MOVE -> {
                val edgeView = _edgeViews.last()
                edgeView.setEndPoint(pointer)
                edgeView.invalidate()
            }

            // Когда отжимаем палец - либо удаляем линию, либо сохраняем её и соединяем пины
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val edgeView = _edgeViews.last()
                val parent =
                    activity.codeField.findViewAt(pointer) { it !is EdgeView }?.parent

                if (parent != null && parent is PinView && parent != this) {
                    val position = activity.codeField.findRelativePosition(parent.contact)
                    val center = position + view.center

                    edgeView.setEndPoint(center)
                    edgeView.invalidate()

                    if (connectWith(parent, edgeView)) {
                        return true
                    }
                }
                _edgeViews.removeLast()
                activity.codeField.removeView(edgeView)
            }
        }
        return true
    }

    /**
     * Соединяем этот пин с [pinView] и передаем [edgeView] для отрисовки линии.
     * Возвращает true, если пины удачно соединились
     */
    private fun connectWith(pinView: PinView, edgeView: EdgeView): Boolean {
        return try {
            sourceCode.connect(pin, pinView.pin)
            pinView._edgeViews.add(edgeView)
            //после соединения нужно спрятать поля, если мы соединили константы
            hideField(pinView)

            true
        }
        // Если попытались соединить PinToOne ко второму пину - убираем первое соединение
        catch (e: AxolotPinOneAdjacentException) {
            if (e.pin == pin) {
                e.pin.adjacent = null
                activity.codeField.removeView(_edgeViews.removeAt(_edgeViews.size - 2))
                return connectWith(pinView, edgeView)
            }
            if (e.pin == pinView.pin) {
                e.pin.adjacent = null
                activity.codeField.removeView(pinView._edgeViews.removeLast())
                return connectWith(pinView, edgeView)
            }
            false
        }
        // Иные
        catch (e: AxolotPinException) {
            false
        }
    }

    /**
     * Прячем все поля для соединенных пинов.
     * Если это был пин типа Boolean, то картинка становится невидимой.
     */
    private fun hideField(pinView: PinView) {
        val inputPinView: PinView
        val inputPin: Pin

        if (this.pin is InputPin) {
            inputPinView = this
            inputPin = this.pin
        } else {
            inputPinView = pinView
            inputPin = pinView.pin
        }

        if (inputPin is DataPin && inputPin is InputPin) {
            if (inputPin is TypedPin) {
                when ((inputPin.type as DeclaredDataPin).type) {
                    Type.INT, Type.FLOAT, Type.STRING -> {
                        inputPinView.inputField.visibility = INVISIBLE
                    }

                    Type.BOOLEAN -> {
                        when {
                            inputPinView.tickIcon.visibility == VISIBLE -> {
                                inputPinView.tickIcon.visibility = INVISIBLE
                            }

                            inputPinView.crossIcon.visibility == VISIBLE -> {
                                inputPinView.crossIcon.visibility = INVISIBLE
                            }

                            else -> throw IllegalStateException("Что-то не так (какая-то проблема с видимостью константы у входящего пина Boolean)")
                        }
                    }

                    else -> throw IllegalStateException("Что-то не так (сокрытие поля у входящего пина)")
                }
            }
        }
    }

    /**
     * Восстановить линию между пинами
     */
    fun restoreEdge(withPinView: PinView): EdgeView {
        val view = binding.contact
        val position = activity.codeField.findRelativePosition(view)
        val center = position + view.center
        val edgeView = EdgeView(context)

        edgeView.position = center
        edgeView.paintBrush.color = color

        edgeView.points.add(Vec2f.ZERO)
        edgeView.points.add(Vec2f.ZERO)

        val withPinViewPosition = activity.codeField.findRelativePosition(withPinView.contact)
        val withPinViewCenter = withPinViewPosition + withPinView.contact.center

        edgeView.setEndPoint(withPinViewCenter)
        hideField(withPinView)

        _edgeViews.add(edgeView)
        withPinView._edgeViews.add(edgeView)

        activity.codeField.addView(edgeView)
        return edgeView
    }

    /**
     * Восстановить значение по умолчанию
     */
    fun restoreConstant() {
        val currentPin = pin

        if (currentPin is InputDataPin && currentPin.adjacent != null
            && currentPin.adjacent is ConstantPin
        ) {

            val constant = (currentPin.adjacent as ConstantPin).constant

            when (constant.type) {
                Type.INT -> binding.inputField.setText(constant.value.toString())
                Type.FLOAT -> binding.inputField.setText(constant.value.toString())
                Type.BOOLEAN -> {
                    if (constant.value as Boolean) binding.tickIcon.visibility = VISIBLE
                    else binding.crossIcon.visibility = VISIBLE
                }
                Type.STRING -> binding.inputField.setText(constant.value.toString())
            }
        }
    }
}