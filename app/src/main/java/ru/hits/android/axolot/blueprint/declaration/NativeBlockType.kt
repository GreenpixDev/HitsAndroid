package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Нативная декларация блока
 */
open class NativeBlockType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType {

    constructor(simpleName: String, vararg pins: DeclaredPin)
            : this(simpleName, pins.toList())

    override val fullName: String
        get() = "native.$simpleName"

    override fun createBlock(): AxolotBlock {
        val block = AxolotBlock(this)

        declaredPins
            .flatMap { it.createPin(block) }
            .forEach { block.addFixedPin(it) }

        return block
    }
}