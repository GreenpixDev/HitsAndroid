package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.FunctionType
import ru.hits.android.axolot.blueprint.declaration.MacrosType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
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
     * Регистрация новой переменной с именем [variableName]
     */
    fun createVariable(variableName: String) {
        require(!hasVariable(variableName)) { "variable $variableName already exists" }
        registerBlock(VariableGetterBlockType(variableName, Type.BOOLEAN))
    }
    fun generateVariableName(): String {
        var counter = 0
        while (hasVariable("var$counter")) {
            counter++
        }
        return "var$counter"
    }

    /**
     * Проврка на существование переменной с именем [variableName]
     */
    fun hasVariable(variableName: String): Boolean {
        val variableGetter = blockTypes["${VariableGetterBlockType.PREFIX_NAME}.$variableName"]
        return variableGetter != null && variableGetter is VariableGetterBlockType
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
     * Изменить тип переменной [variableName]
     */
    fun retypeVariable(variableName: String, newType: VariableType<*>) {
        getVariableGetter(variableName).variableType = newType
    }

    /**
     * Создать функцию
     */
    fun createFunction(name: String): FunctionType {
        require("${FunctionType.PREFIX_NAME}.$name" !in blockTypes) {
            "function with name $name already exists"
        }
        val functionType = FunctionType(name)
        functionType.beginBlock = functionType.createBlock(functionType.beginType)

        registerBlock(functionType)
        return functionType
    }

    /**
     * Создать макрос
     */
    fun createMacros(name: String): MacrosType {
        require("${MacrosType.PREFIX_NAME}.$name" !in blockTypes) {
            "macros with name $name already exists"
        }
        val macrosType = MacrosType(name)
        macrosType.beginBlock = macrosType.createBlock(macrosType.beginType)
        macrosType.endBlock = macrosType.createBlock(macrosType.endType)

        registerBlock(macrosType)
        return macrosType
    }

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            program.mainBlock = program.createBlock(AxolotNativeLibrary.BLOCK_MAIN)
            return program
        }

    }
}