package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.*
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
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

    lateinit var beginBlock: AxolotBlock
    lateinit var endBlock: AxolotBlock

    val beginType = MacrosBeginType(this)
    val endType = MacrosEndType(this)

    val inputFlow = mutableSetOf<String>()
    val outputFlow = mutableSetOf<String>()
    val inputData = mutableMapOf<String, VariableType<*>>()
    val outputData = mutableMapOf<String, VariableType<*>>()

    override val simpleName: String
        get() = macrosName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>()

    fun addInputFlow(inputName: String) {
        val pin = DeclaredSingleInputFlowPin(
            nodeFabric = { NodeMacrosInput(inputName) },
            lazyName = { inputName }
        )
        declaredPins.add(pin)
        beginType.addFlow(inputName).createAllPin(endBlock).forEach {
            beginBlock.contacts.add(it)
        }
    }

    fun addInputData(inputName: String, type: VariableType<*>) {
        val pin = DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosDependency>()
                    .find { it.name == inputName }
                    ?.let { it.init(node) }
            },
            lazyName = { inputName },
            lazyType = { type }
        )
        declaredPins.add(pin)
        beginType.addData(inputName, type).createAllPin(endBlock).forEach {
            beginBlock.contacts.add(it)
        }
    }

    fun addOutputFlow(outputName: String) {
        val pin = DeclaredSingleOutputFlowPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosInput>()
                    .find { it.name == outputName }
                    ?.let { it.nextNode = node }
            },
            lazyName = { outputName }
        )
        declaredPins.add(pin)
        endType.addFlow(outputName).createAllPin(endBlock).forEach {
            endBlock.contacts.add(it)
        }
    }

    fun addOutputData(outputName: String, type: VariableType<*>) {
        val pin = DeclaredSingleOutputDataPin(
            nodeFabric = { NodeMacrosDependency(outputName) },
            lazyName = { outputName },
            lazyType = { type }
        )
        declaredPins.add(pin)
        endType.addData(outputName, type).createAllPin(endBlock).forEach {
            endBlock.contacts.add(it)
        }
    }

}