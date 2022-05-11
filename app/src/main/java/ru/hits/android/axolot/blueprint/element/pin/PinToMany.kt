package ru.hits.android.axolot.blueprint.element.pin

interface PinToMany : Pin {

    val adjacent: MutableCollection<PinToOne>

    infix fun connect(to: PinToOne) {
        adjacent.add(to)
        to.adjacent = this
    }

}