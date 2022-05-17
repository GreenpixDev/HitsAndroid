package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.interpreter.type.VariableType
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput
import java.io.Serializable

/**
 * Класс исполняемой программы на языке Axolot.
 */
class AxolotProgram private constructor() : AxolotBaseSource(), AxolotProject, Serializable {


    override val variableTypes = mutableMapOf<String, VariableType<*>>()

    override val  blockTypes = mutableMapOf<String, BlockType>()

    var mainBlock: AxolotBlock? = null
        @Throws(AxolotException::class)
        set(value) {
            if (value != null && field != null) {
                throw AxolotException { "The main block is already in program" }
            }
            field = value
        }

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            return program
        }

    }
}