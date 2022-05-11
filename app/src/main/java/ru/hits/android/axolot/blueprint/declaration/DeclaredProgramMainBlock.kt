package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin

/**
 * Нативная декларация начала программы
 */
class DeclaredProgramMainBlock(
    vararg pins: DeclaredPin,
) : DeclaredNativeBlock(pins.toList()) {

}