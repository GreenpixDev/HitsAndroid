package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor

/**
 * Декларация начала макроса
 */
class MacrosBeginType(
    var macrosType: MacrosType
) : AxolotBaseSource(), BlockType {

    override val simpleName: String
        get() = macrosType.simpleName

    override val fullName: String
        get() = "${MacrosType.PREFIX_NAME}.begin_$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>()

    override fun getDisplayName(context: Context): String {
        return context.getLocalizedString("macros_input")
    }

    fun addFlow(name: String): DeclaredSingleOutputFlowPin {
        val pin = DeclaredSingleOutputFlowPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosInput>()
                    .find { it.name == name }
                    ?.let { it.nextNode = node }
            },
            lazyName = { name }
        )
        declaredPins.add(pin)
        return pin
    }

    fun addData(name: String, type: VariableType<*>): DeclaredSingleOutputDataPin {
        val pin = DeclaredSingleOutputDataPin(
            nodeFabric = { NodeMacrosDependency(name) },
            lazyName = { name },
            lazyType = { type }
        )
        declaredPins.add(pin)
        return pin
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderMacros)
    }

}