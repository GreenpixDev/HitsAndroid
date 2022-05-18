package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.util.Vec2f

/**
 * Класс блока на плоскости.
 * Содержит в себе тип блока (декларацию блока), а также
 */
class AxolotBlock(val type: BlockType) : AxolotOwner {

    /**
     * Координата X для UI
     */
    var position: Vec2f? = null

    /**
     * Множество контактов (пинов) у блока
     */
    val contacts = linkedSetOf<Pin>()

}