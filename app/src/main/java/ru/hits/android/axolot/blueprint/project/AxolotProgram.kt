package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.*
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.filterIsInstance

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
        registerBlock(VariableSetterBlockType(variableName, Type.BOOLEAN))
    }

    /**
     * Проврка на существование переменной с именем [variableName]
     */
    fun hasVariable(variableName: String): Boolean {
        return "${VariableGetterBlockType.PREFIX_NAME}.$variableName" in blockTypes
    }

    /**
     * Получить блок GetterVariable у переменной [variableName]
     */
    fun getVariableGetter(variableName: String): VariableGetterBlockType {
        val variableGetter = blockTypes["${VariableGetterBlockType.PREFIX_NAME}.$variableName"]

        if (variableGetter == null || variableGetter !is VariableGetterBlockType) {
            throw AxolotException { "cannot find variable getter $variableName" }
        }
        return variableGetter
    }

    /**
     * Получить блок SetterVariable у переменной [variableName]
     */
    fun getVariableSetter(variableName: String): VariableSetterBlockType {
        val variableSetter = blockTypes["${VariableSetterBlockType.PREFIX_NAME}.$variableName"]

        if (variableSetter == null || variableSetter !is VariableSetterBlockType) {
            throw AxolotException { "cannot find variable setter $variableName" }
        }
        return variableSetter
    }

    /**
     * Переименовать переменную [variableName]
     */
    fun renameVariable(variableName: String, newName: String) {
        val variableGetter = getVariableGetter(variableName)
        val variableSetter = getVariableSetter(variableName)
        variableGetter.variableName = newName
        variableSetter.variableName = newName

        blockTypes.remove("${VariableGetterBlockType.PREFIX_NAME}.$variableName")
        blockTypes["${VariableGetterBlockType.PREFIX_NAME}.$newName"] = variableGetter
        blockTypes.remove("${VariableSetterBlockType.PREFIX_NAME}.$variableName")
        blockTypes["${VariableSetterBlockType.PREFIX_NAME}.$newName"] = variableSetter

        findAllBlockByType(variableGetter)
            .flatMap { it.contacts }
            .forEach { it.name = newName }

        findAllBlockByType(variableSetter)
            .flatMap { it.contacts }
            .filterIsInstance<OutputPin>()
            .forEach { it.name = newName }
    }

    /**
     * Изменить тип переменной [variableName]
     */
    fun retypeVariable(variableName: String, newType: VariableType<*>) {
        getVariableGetter(variableName).variableType = newType
        getVariableSetter(variableName).variableType = newType
    }

    /**
     * Проврка на существование функции с именем [functionName]
     */
    fun hasFunction(functionName: String): Boolean {
        return "${FunctionType.PREFIX_NAME}.$functionName" in blockTypes
    }

    /**
     * Создать функцию
     */
    fun createFunction(functionName: String): FunctionType {
        require(!hasFunction(functionName)) { "function $functionName already exists" }
        val functionType = FunctionType(functionName)

        functionType.beginBlock = functionType.createBlock(functionType.beginType)

        registerBlock(functionType)
        return functionType
    }

    /**
     * Получить блок FunctionType у функции [functionName]
     */
    fun getFunction(functionName: String): FunctionType {
        val function = blockTypes["${FunctionType.PREFIX_NAME}.$functionName"]

        if (function == null || function !is FunctionType) {
            throw AxolotException { "cannot find function $functionName" }
        }
        return function
    }

    /**
     * Переименовать функцию [functionName]
     */
    fun renameFunction(functionName: String, newName: String) {
        val function = getFunction(functionName)
        function.functionName = newName

        blockTypes.remove("${FunctionType.PREFIX_NAME}.$functionName")
        blockTypes["${FunctionType.PREFIX_NAME}.$newName"] = function
    }

    /**
     * Проврка на существование макроса с именем [macrosName]
     */
    fun hasMacros(macrosName: String): Boolean {
        return "${MacrosType.PREFIX_NAME}.$macrosName" in blockTypes
    }

    /**
     * Получить блок MacrosType у макроса [macrosName]
     */
    fun getMacros(macrosName: String): MacrosType {
        val macros = blockTypes["${MacrosType.PREFIX_NAME}.$macrosName"]

        if (macros == null || macros !is MacrosType) {
            throw AxolotException { "cannot find macros $macrosName" }
        }
        return macros
    }

    /**
     * Переименовать макрос [macrosName]
     */
    fun renameMacros(macrosName: String, newName: String) {
        val macros = getMacros(macrosName)
        macros.macrosName = newName

        blockTypes.remove("$${MacrosType.PREFIX_NAME}.$macrosName")
        blockTypes["${MacrosType.PREFIX_NAME}.$newName"] = macros
    }

    /**
     * Создать макрос
     */
    fun createMacros(macrosName: String): MacrosType {
        require(!hasMacros(macrosName)) { "macros $macrosName already exists" }
        val macrosType = MacrosType(macrosName)

        macrosType.beginBlock = macrosType.createBlock(macrosType.beginType)
        macrosType.endBlock = macrosType.createBlock(macrosType.endType)

        registerBlock(macrosType)
        return macrosType
    }

    /**
     * Получить все все все блоки типа [type]
     */
    fun findAllBlockByType(type: BlockType): List<AxolotBlock> {
        val list = findBlockByType(type).toMutableList()
        blockTypes
            .filterIsInstance<String, AxolotSource>()
            .forEach { list.addAll(it.value.findBlockByType(type)) }
        return list
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