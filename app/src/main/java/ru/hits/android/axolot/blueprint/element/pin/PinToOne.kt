package ru.hits.android.axolot.blueprint.element.pin

interface PinToOne : Pin {

    var adjacent: PinToMany?

    infix fun connect(to: PinToMany) {
        adjacent = to
        to.adjacent.add(this)
    }

}