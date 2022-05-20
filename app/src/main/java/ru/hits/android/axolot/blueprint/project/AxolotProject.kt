package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.interpreter.type.VariableType
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

/**
 * Общий интерфейс проекта на языке Axolot.
 * Это может быть библиотека или полноценная программа.
 */
interface AxolotProject {

    val variableTypes: MutableMap<String, VariableType<*>>

    val blockTypes: MutableMap<String, BlockType>

    /**
     * Добавить библиотеку [library] к проекту
     */
    fun addLibrary(library: AxolotLibrary) {
        blockTypes.putAll(library.blockTypes)
        variableTypes.putAll(library.variableTypes)
    }

    /**
     * Регистрация типа блока [blockType]
     */
    fun registerBlock(blockType: BlockType) {
        blockTypes[blockType.fullName] = blockType
    }

}