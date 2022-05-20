package ru.hits.android.axolot.blueprint.element.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner

interface Pin {

    val adjacentPins: Collection<Pin>

    var name: String

    val owner: AxolotOwner

    fun clear()

}