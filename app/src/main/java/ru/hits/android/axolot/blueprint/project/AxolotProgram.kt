package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.impl.*
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Класс исполняемой программы на языке Axolot.
 */
class AxolotProgram private constructor() : AxolotBaseSource(), AxolotProject {

    fun deleteBlock(block: AxolotBlock) {
        for (i in blockTypes.values) {
            if (block.type == i) {
                for (contact in block.contacts) {
                    if (contact is ConstantPin) {
                        contact.clear()
                    }
                    if (contact is InputDataPin) {
                        contact.clear()
                    }
                    if (contact is InputFlowPin) {
                        contact.clear()
                    }
                    if (contact is OutputDataPin) {
                        contact.clear()
                    }
                    if (contact is OutputFlowPin) {
                        contact.clear()
                    }
                }
            }
        }
        blocks.remove(block)
    }

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

    /**
     * Генерация первого уникального имени по шаблону var<число>
     */
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

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            return program
        }

    }
}