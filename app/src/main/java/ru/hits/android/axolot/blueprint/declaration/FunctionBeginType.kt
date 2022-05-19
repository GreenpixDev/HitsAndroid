package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Декларация начала функции
 */
class FunctionBeginType(
    val functionType: FunctionType
) : BlockType {

    override val simpleName: String
        get() = functionType.simpleName

    override val fullName: String
        get() = "${FunctionType.PREFIX_NAME}.begin_$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>(
        DeclaredSingleOutputFlowPin { _, _ -> }
    )

    fun addParameter(parameterName: String, type: VariableType<*>) {
        declaredPins.add(DeclaredSingleOutputDataPin(
            nodeFabric = { NodeFunctionParameter() },
            lazyName = { parameterName },
            lazyType = { type }
        ))
    }
}