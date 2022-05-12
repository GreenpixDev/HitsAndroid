package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock

/**
 * Общий интерфейс декларируемого блока (или его типа),
 * на основе которого можно создать блок на плоскости
 */
interface BlockType {

    /**
     * Простое название блока.
     * Например, нативный блок Branch будет иметь простое название "branch"
     */
    val simpleName: String

    /**
     * Полное название типа блока.
     * Например, нативный блок Branch будет иметь полное название "native.branch"
     */
    val fullName: String

    /**
     * Список декларируемых пинов
     */
    val declaredPins: List<DeclaredPin>

    /**
     * Создать блок на основе этого типа блока
     */
    fun createBlock(): AxolotBlock

}