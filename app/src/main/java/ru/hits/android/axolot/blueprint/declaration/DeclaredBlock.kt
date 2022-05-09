package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Общий интерфейс декларируемого блока (или его типа),
 * на основе которого можно создать блок на плоскости
 */
interface DeclaredBlock {

    /**
     * Список декларируемых пинов
     */
    val declaredPins: List<DeclaredPin>

    /**
     * Создать блок на основе этого типа блока
     */
    fun createBlock(): AxolotBlock

}