package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Декларация макроса
 */
class MacrosType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType {

    override val fullName: String
        get() = "macros.$simpleName"

    override fun createBlock(): AxolotBlock {
        TODO("Not yet implemented")
    }

}