package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Нативная декларация блока
 */
open class DeclaredNativeBlock(
    override val declaredPins: List<DeclaredPin>,
) : DeclaredBlock {

    constructor(vararg pins: DeclaredPin)
            : this(pins.toList())

    override fun createBlock(): AxolotBlock {
        val block = AxolotBlock(this)

        declaredPins
            .flatMap { it.createPin(block) }
            .forEach { block.addFixedPin(it) }

        return block
    }
}