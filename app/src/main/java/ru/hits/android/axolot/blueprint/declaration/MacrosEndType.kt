package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosOutput
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor

/**
 * Декларация конца макроса
 */
class MacrosEndType(
    var macrosType: MacrosType
) : AxolotBaseSource(), BlockType {

    override val simpleName: String
        get() = macrosType.simpleName

    override val fullName: String
        get() = "${MacrosType.PREFIX_NAME}.end_$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>()

    override fun getDisplayName(context: Context): String {
        return context.getLocalizedString("macros_output")
    }

    fun addFlow(name: String): DeclaredSingleInputFlowPin {
        val pin = DeclaredSingleInputFlowPin(
            nodeFabric = { NodeMacrosOutput(name) },
            lazyName = { name }
        )
        declaredPins.add(pin)
        return pin
    }

    fun addData(
        lazyName: () -> String,
        lazyType: () -> VariableType<*>
    ): DeclaredSingleInputDataPin {
        val pin = DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosDependency>()
                    .find { it.name == lazyName.invoke() }
                    ?.let { it.init(node) }
            },
            lazyName = lazyName,
            lazyType = lazyType
        )
        declaredPins.add(pin)
        return pin
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderMacros)
    }

}