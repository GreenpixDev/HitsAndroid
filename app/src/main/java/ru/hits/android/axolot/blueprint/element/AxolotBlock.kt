package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.element.pin.Pin

/**
 * Класс блока на плоскости.
 * Содержит в себе тип блока (декларацию блока), а также
 */
class AxolotBlock(val type: BlockType) : AxolotOwner {

    /**
     * Координата X для UI
     */
    var x = 0.0

    /**
     * Координата Y для UI
     */
    var y = 0.0

    /**
     * Множество контактов (пинов) у блока
     */
    val contacts = linkedSetOf<Pin>()

    fun addFixedPin(pin: Pin) {
        contacts.add(pin)
    }
}