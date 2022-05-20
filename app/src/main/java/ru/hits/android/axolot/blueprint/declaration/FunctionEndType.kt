package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor

/**
 * Декларация конца функции
 */
class FunctionEndType(
    val functionType: FunctionType
) : BlockType {

    override val simpleName: String
        get() = functionType.simpleName

    override val fullName: String
        get() = "${FunctionType.PREFIX_NAME}.end_$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>(
        DeclaredSingleInputFlowPin(
            nodeFabric = { NodeFunctionEnd() }
        )
    )

    override fun getDisplayName(context: Context): String {
        return context.getLocalizedString("function_end")
    }

    fun addResult(lazyName: () -> String, lazyType: () -> VariableType<*>) {
        declaredPins.add(
            DeclaredSingleInputDataPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeFunctionEnd>()
                        .first().dependencies[lazyName.invoke()] = node
                },
                lazyName = lazyName,
                lazyType = lazyType
            )
        )
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderFunction)
    }

    override fun createBlock(): AxolotBlock {
        val block = super.createBlock()
        functionType.endBlocks.add(block)
        return block
    }

}