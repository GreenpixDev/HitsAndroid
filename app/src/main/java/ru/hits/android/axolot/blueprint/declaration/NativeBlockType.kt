package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin

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

}