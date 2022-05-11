package ru.hits.android.axolot.blueprint.element.pin

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin

interface TypedPin : Pin {

    val type: DeclaredPin

}