package ru.hits.android.axolot.blueprint.element.pin.impl

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.*

class InputFlowPin @JvmOverloads constructor(
    override val owner: AxolotOwner,
    override val type: DeclaredPin,
    override val name: String = ""
) : FlowPin, PinToMany, InputPin, TypedPin {

    override var adjacent = mutableListOf<PinToOne>()
    fun clear() {
        for (i in adjacent) {
            i.adjacent?.adjacent?.remove(i)
        }
    }
}