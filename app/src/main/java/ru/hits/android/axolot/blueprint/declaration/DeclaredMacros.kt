package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Декларация макроса
 */
class DeclaredMacros(
    override val declaredPins: List<DeclaredPin>
) : DeclaredBlock {

    override fun createBlock(): AxolotBlock {
        TODO("Not yet implemented")
    }

}