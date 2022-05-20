package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
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
    fun createBlock(): AxolotBlock {
        val block = AxolotBlock(this)

        declaredPins
            .flatMap { it.createAllPin(block) }
            .forEach { block.contacts.add(it) }

        return block
    }

    /**
     * Отображаемое имя
     */
    fun getDisplayName(context: Context): String

    /**
     * Отображаемый цвет заголовка
     */
    fun getDisplayColor(context: Context): Int

}