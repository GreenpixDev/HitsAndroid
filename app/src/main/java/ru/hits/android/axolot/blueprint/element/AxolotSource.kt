package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.element.pin.Pin

/**
 * Интерфейс исходного кода.
 * Исходны код содержит множество блоков.
 * TODO в данный момент содержит ещё и множество контактов, т.е. пинов, возможно удалится в будущем
 */
interface AxolotSource {

    val blocks: MutableSet<AxolotBlock>

    val contacts: MutableSet<Pin>

}