package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Общий интерфейс проекта на языке Axolot.
 * Это может быть библиотека или полноценная программа.
 */
interface AxolotProject {

    val variableTypes: MutableMap<String, VariableType<*>>

    val blockTypes: MutableMap<String, BlockType>

    fun addLibrary(library: AxolotLibrary) {
        blockTypes.putAll(library.blockTypes)
        variableTypes.putAll(library.variableTypes)
    }

}