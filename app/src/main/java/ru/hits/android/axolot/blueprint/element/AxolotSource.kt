package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.exception.AxolotPinException
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Интерфейс исходного кода.
 * Исходны код содержит множество блоков.
 * TODO в данный момент содержит ещё и множество контактов, т.е. пинов, возможно удалится в будущем
 */
interface AxolotSource {

    val blocks: MutableSet<AxolotBlock>

    /**
     * Добавить новый блок на плоскость
     */
    fun addBlock(block: AxolotBlock)

    /**
     * Задать значение булавке (если она не соединена с другими)
     */
    fun <T> setValue(pin: InputDataPin, type: VariableType<T>, value: T)

    /**
     * Соединить булавки друг с другом
     */
    @Throws(AxolotPinException::class)
    fun connect(from: Pin, to: Pin)

    /**
     * Отсоединить булавку от всех прикрепленных к ней булавок
     */
    fun disconnect(pin: Pin)

}