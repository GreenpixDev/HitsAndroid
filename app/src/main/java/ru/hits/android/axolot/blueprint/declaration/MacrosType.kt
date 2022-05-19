package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Декларация макроса
 */
class MacrosType(
    var macrosName: String
) : AxolotBaseSource(), BlockType {

    companion object {
        const val PREFIX_NAME = "macros"
    }

    val inputFlow = mutableSetOf<String>()
    val outputFlow = mutableSetOf<String>()
    val inputData = mutableMapOf<String, VariableType<*>>()
    val outputData = mutableMapOf<String, VariableType<*>>()

    override val simpleName: String
        get() = macrosName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>()

    /*fun addInputData(inputName: String, type: VariableType<*>) {
        declaredPins.add(declaredPins.size - 1, DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosDependency>()
                    .first().dependencies[parameterName] = node
            },
            lazyName = { inputName },
            lazyType = { type }
        ))
        beginType.addParameter(parameterName, type)
    }

    fun addOutputData(outputName: String, type: VariableType<*>) {
        declaredPins.add(declaredPins.size - 1, DeclaredSingleOutputDataPin(
            nodeFabric = { NodeMacrosDependency() },
            lazyName = { outputName },
            lazyType = { type }
        ))
        endType.addResult(resultName, type)
    }*/

}