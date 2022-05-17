package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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

    private val edgeViews = mutableListOf<EdgeView>()

    lateinit var pin: Pin

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
     * Обновить пин (если изменился цвет)
     */
    fun update() {
        //
        //binding.contact.setColorFilter(color)
        // TODO
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
                edgeView.paintBrush.color = color

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

                    if (connectWith(parent, edgeView)) {
                        return true
                    }
                }
                edgeViews.removeLast()
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
            sourceCode.connect(pin, pinView.pin)        //здесь выкидывает все возможные ошибки
            pinView.edgeViews.add(edgeView)

            //тут дальше логика какая-то логика должна быть

            var someVar = pinView.pin
            if (someVar is DataPin && someVar is InputPin) {
                //тип данных и тип входной (который слева)
                if (someVar is TypedPin) {
                    //пин с типом
                    if ((someVar.type as DeclaredDataPin).type == Type.INT) {

                    }
                }
            }

            true
        }
        // Если попытались соединить PinToOne ко второму пину - убираем первое соединение
        catch (e: AxolotPinOneAdjacentException) {
            if (e.pin == pin) {
                e.pin.adjacent = null
                activity.codeField.removeView(edgeViews.removeAt(edgeViews.size - 2))
                return connectWith(pinView, edgeView)
            }
            if (e.pin == pinView.pin) {
                e.pin.adjacent = null
                activity.codeField.removeView(pinView.edgeViews.removeLast())
                return connectWith(pinView, edgeView)
            }
            false
        }
        // Иные
        catch (e: AxolotPinException) {
            false
        }
    }
}