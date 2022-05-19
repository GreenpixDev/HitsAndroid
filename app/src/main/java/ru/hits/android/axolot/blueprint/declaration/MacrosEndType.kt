package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosOutput
import ru.hits.android.axolot.interpreter.type.VariableType

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

    fun addFlow(name: String): DeclaredSingleInputFlowPin {
        val pin = DeclaredSingleInputFlowPin(
            nodeFabric = { NodeMacrosOutput(name) },
            lazyName = { name }
        )
        declaredPins.add(pin)
        return pin
    }

    fun addData(name: String, type: VariableType<*>): DeclaredSingleInputDataPin {
        val pin = DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosDependency>()
                    .find { it.name == name }
                    ?.let { it.init(node) }
            },
            lazyName = { name },
            lazyType = { type }
        )
        declaredPins.add(pin)
        return pin
    }

}