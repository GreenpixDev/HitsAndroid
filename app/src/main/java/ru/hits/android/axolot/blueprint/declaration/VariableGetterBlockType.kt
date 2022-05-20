package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.interpreter.node.function.NodeGetVariable
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor

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

    override fun getDisplayName(context: Context): String {
        return context.getLocalizedString(PREFIX_NAME)
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor("colorVariable${variableType}")
    }

}