package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import java.io.Serializable

/**
 * Нативная декларация блока
 */
open class NativeBlockType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType, Serializable {

    constructor(simpleName: String, vararg pins: DeclaredPin)
            : this(simpleName, pins.toList())

    override val fullName: String
        get() = "native.$simpleName"

    override fun createBlock(): AxolotBlock {
        val block = AxolotBlock(this)

        declaredPins
            .flatMap { it.createPin(block) }
            .forEach { block.contacts.add(it) }

        return block
    }
}