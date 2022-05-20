package ru.hits.android.axolot.blueprint.element.pin

interface PinToOne : Pin {

    var adjacent: PinToMany?

    override val adjacentPins: Collection<Pin>
        get() = listOfNotNull(adjacent)

    override fun clear() {
        adjacent?.adjacent?.remove(this)
        adjacent = null
    }

    infix fun connect(to: PinToMany) {
        adjacent = to
        to.adjacent.add(this)
    }
}