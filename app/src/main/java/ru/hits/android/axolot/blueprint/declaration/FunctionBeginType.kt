package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getThemeColor

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

    override fun getDisplayName(context: Context): String {
        return functionType.getDisplayName(context)
    }

    fun addParameter(lazyName: () -> String, lazyType: () -> VariableType<*>) {
        declaredPins.add(
            DeclaredSingleOutputDataPin(
                nodeFabric = { NodeFunctionParameter() },
                lazyName = lazyName,
                lazyType = lazyType
            )
        )
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderFunction)
    }

}