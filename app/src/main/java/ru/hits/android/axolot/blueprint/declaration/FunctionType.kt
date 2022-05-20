package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getThemeColor

/**
 * Декларация функции
 */
class FunctionType(
    var functionName: String
) : AxolotBaseSource(), BlockType {

    companion object {
        const val PREFIX_NAME = "function"
        private var counter = 0L
    }

    lateinit var beginBlock: AxolotBlock
    val endBlocks = mutableListOf<AxolotBlock>()
    val invocationBlocks = mutableListOf<AxolotBlock>()

    val beginType = FunctionBeginType(this)
    val endType = FunctionEndType(this)

    private val inputNames = mutableMapOf<Long, String>()
    private val outputNames = mutableMapOf<Long, String>()
    private val inputTypes = mutableMapOf<Long, VariableType<*>>()
    private val outputTypes = mutableMapOf<Long, VariableType<*>>()

    override val simpleName: String
        get() = functionName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override fun getDisplayName(context: Context): String {
        return functionName
    }

    override val declaredPins = mutableListOf(
        DeclaredSingleInputFlowPin(
            nodeFabric = { NodeFunctionInvoke() }
        ),
        DeclaredSingleOutputFlowPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeFunctionInvoke>()
                    .first().nextNode = node
            }
        )
    )

    override fun createBlock(): AxolotBlock {
        val block = super<BlockType>.createBlock()
        invocationBlocks.add(block)
        return block
    }

    fun addInput(parameterName: String, type: VariableType<*>) {
        val identifier = counter++
        val pin = DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeFunctionInvoke>()
                    .first().dependencies[inputNames[identifier]!!] = node
            },
            lazyName = { inputNames[identifier]!! },
            lazyType = { inputTypes[identifier]!! }
        )
        inputNames[identifier] = parameterName
        inputTypes[identifier] = type

        declaredPins.add(pin)
        beginType.addParameter({ inputNames[identifier]!! }, { inputTypes[identifier]!! })

        beginBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    fun addOutput(resultName: String, type: VariableType<*>) {
        val identifier = counter++
        val pin = DeclaredSingleOutputDataPin(
            nodeFabric = { NodeFunctionReturned(resultName) },
            lazyName = { outputNames[identifier]!! },
            lazyType = { outputTypes[identifier]!! }
        )
        outputNames[identifier] = resultName
        outputTypes[identifier] = type

        declaredPins.add(pin)
        endType.addResult({ outputNames[identifier]!! }, { outputTypes[identifier]!! })

        endBlocks.forEach { it.update() }
        invocationBlocks.forEach { it.update() }
    }

    fun hasInput(name: String): Boolean {
        return inputNames.any { it.value == name }
    }

    fun renameInput(name: String, newName: String) {
        inputNames
            .filter { it.value == name }
            .onEach { inputNames[it.key] = newName }
    }

    fun retypeInput(name: String, newType: VariableType<*>) {
        inputNames
            .filter { it.value == name }
            .onEach { inputTypes[it.key] = newType }
    }

    fun hasOutput(name: String): Boolean {
        return outputNames.any { it.value == name }
    }

    fun renameOutput(name: String, newName: String) {
        outputNames
            .filter { it.value == name }
            .onEach { outputNames[it.key] = newName }
    }

    fun retypeOutput(name: String, newType: VariableType<*>) {
        outputNames
            .filter { it.value == name }
            .onEach { outputTypes[it.key] = newType }
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderFunction)
    }

}