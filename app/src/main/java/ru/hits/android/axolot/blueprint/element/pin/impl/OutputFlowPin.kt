package ru.hits.android.axolot.blueprint.element.pin.impl

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.*

class OutputFlowPin @JvmOverloads constructor(
    override val owner: AxolotOwner,
    override val type: DeclaredPin,
    override val name: String = ""
) : FlowPin, PinToOne, OutputPin, TypedPin {

    override var adjacent: PinToMany? = null
    fun clear() {
        adjacent?.adjacent?.remove(this)
    }
}