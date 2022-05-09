package ru.hits.android.axolot.blueprint.element.pin.impl

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.*

class InputDataPin @JvmOverloads constructor(
    override val owner: AxolotOwner,
    override val type: DeclaredPin,
    override val name: String = ""
) : DataPin, PinToOne, InputPin, TypedPin {

    override var adjacent: PinToMany? = null

}