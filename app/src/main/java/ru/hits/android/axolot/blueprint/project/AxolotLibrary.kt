package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Класс библиотеки для языка Axolot.
 * Можно подключать к другим библиотекам или программам.
 */
open class AxolotLibrary : AxolotProject {

    override val variableTypes = mutableMapOf<String, VariableType<*>>()

    override val blockTypes = mutableMapOf<String, BlockType>()

    companion object {

        fun create(): AxolotLibrary {
            val library = AxolotLibrary()
            library.addLibrary(AxolotDefaultLibrary())
            return library
        }

    }

}