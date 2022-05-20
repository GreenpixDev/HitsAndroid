package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.interpreter.node.function.NodeGetVariable
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Декларация макроса
 */
class VariableGetterBlockType(
    var variableName: String,
    var variableType: VariableType<*>
) : BlockType {

    companion object {
        const val PREFIX_NAME = "native.getVariable"
    }

    override val simpleName: String
        get() = variableName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override val declaredPins = listOf<DeclaredPin>(
        DeclaredSingleOutputDataPin(
            nodeFabric = { NodeGetVariable(variableName) },
            lazyName = { variableName },
            lazyType = { variableType }
        )
    )

}