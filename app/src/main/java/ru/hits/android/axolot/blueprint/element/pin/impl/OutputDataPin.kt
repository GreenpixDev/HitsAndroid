package ru.hits.android.axolot.blueprint.element.pin.impl

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.*

class OutputDataPin @JvmOverloads constructor(
    override val owner: AxolotOwner,
    override val type: DeclaredPin,
    override var name: String = ""
) : DataPin, PinToMany, OutputPin, TypedPin {

    override var adjacent = mutableListOf<PinToOne>()

}