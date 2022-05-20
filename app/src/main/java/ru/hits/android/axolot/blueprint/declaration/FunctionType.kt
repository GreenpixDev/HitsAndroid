package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin

/**
 * Декларация функции
 */
class FunctionType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType {

    override val fullName: String
        get() = "function.$simpleName"

}