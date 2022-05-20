package ru.hits.android.axolot.blueprint.element.pin.impl

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.DataPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.interpreter.variable.Variable

class ConstantPin(
    val constant: Variable,
    override val owner: AxolotOwner
) : DataPin, PinToMany, OutputPin {

    override var name = ""

    override var adjacent = mutableListOf<PinToOne>()

}