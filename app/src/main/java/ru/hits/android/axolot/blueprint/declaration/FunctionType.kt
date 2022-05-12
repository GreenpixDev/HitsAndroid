package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Декларация функции
 */
class FunctionType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType {

    override val fullName: String
        get() = "function.$simpleName"

    override fun createBlock(): AxolotBlock {
        TODO("Not yet implemented")
    }

}