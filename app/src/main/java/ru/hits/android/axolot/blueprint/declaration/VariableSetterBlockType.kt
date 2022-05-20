package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.interpreter.node.executable.NodeSetVariable
import ru.hits.android.axolot.interpreter.node.function.NodeGetVariable
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Декларация макроса
 */
class VariableSetterBlockType(
    var variableName: String,
    var variableType: VariableType<*>
) : BlockType {

    companion object {
        const val PREFIX_NAME = "native.setVariable"
    }

    override val simpleName: String
        get() = variableName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override val declaredPins = listOf(
        DeclaredSingleInputFlowPin(
            nodeFabric = { NodeSetVariable(variableName) }
        ),
        DeclaredSingleOutputFlowPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeSetVariable>()
                    .first().nextNode = node
            },
        ),
        DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeSetVariable>()
                    .first().dependencies[NodeSetVariable.INPUT] = node
            },
            lazyName = { "value" },
            lazyType = { variableType }
        ),
        DeclaredSingleOutputDataPin(
            nodeFabric = { NodeGetVariable(variableName) },
            lazyName = { variableName },
            lazyType = { variableType }
        )
    )

}