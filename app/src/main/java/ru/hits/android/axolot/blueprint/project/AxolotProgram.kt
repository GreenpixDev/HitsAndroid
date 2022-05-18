package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Класс исполняемой программы на языке Axolot.
 */
class AxolotProgram private constructor() : AxolotBaseSource(), AxolotProject {

    override val variableTypes = mutableMapOf<String, VariableType<*>>()

    override val blockTypes = mutableMapOf<String, BlockType>()

    var mainBlock: AxolotBlock? = null
        @Throws(AxolotException::class)
        set(value) {
            if (value != null && field != null) {
                throw AxolotException { "The main block is already in program" }
            }
            field = value
        }

    /**
     * Регистрация новой переменной
     */
    fun createVariable(name: String) {
        registerBlock(VariableGetterBlockType(name, Type.BOOLEAN))
    }

    /**
     * Получить блок GetVariable у переменной [variableName]
     */
    fun getVariableGetter(variableName: String): VariableGetterBlockType {
        val variableGetter = blockTypes["${VariableGetterBlockType.PREFIX_NAME}.$variableName"]

        if (variableGetter == null || variableGetter !is VariableGetterBlockType) {
            throw AxolotException { "cannot find variable $variableName" }
        }
        return variableGetter
    }

    /**
     * Переименовать переменную [variableName]
     */
    fun renameVariable(variableName: String, newName: String) {
        val variableGetter = getVariableGetter(variableName)
        variableGetter.variableName = newName

        blockTypes.remove("${VariableGetterBlockType.PREFIX_NAME}.$variableName")
        blockTypes["native.getVariable.$newName"] = variableGetter
    }

    /**
     * Изменить тип переменной [name]
     */
    fun retypeVariable(variableName: String, newType: VariableType<*>) {
        getVariableGetter(variableName).variableType = newType
    }

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            return program
        }

    }
}