package ru.hits.android.axolot.blueprint.element.pin

interface PinToMany : Pin {

    val adjacent: MutableCollection<PinToOne>

    override val adjacentPins: Collection<Pin>
        get() = adjacent

    override fun clear() {
        adjacent.forEach { it.adjacent = null }
        adjacent.clear()
    }

    infix fun connect(to: PinToOne) {
        adjacent.add(to)
        to.adjacent = this
    }

}