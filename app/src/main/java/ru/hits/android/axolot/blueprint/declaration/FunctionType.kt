package ru.hits.android.axolot.blueprint.declaration

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.type.VariableType

/**
 * Декларация функции
 */
class FunctionType(
    var functionName: String
) : AxolotBaseSource(), BlockType {

    companion object {
        const val PREFIX_NAME = "function"
    }

    lateinit var beginBlock: AxolotBlock
    val invocationBlocks = mutableListOf<AxolotBlock>()

    val beginType = FunctionBeginType(this)
    val endType = FunctionEndType(this)

    val output = mutableMapOf<String, VariableType<*>>()
    val input = mutableMapOf<String, VariableType<*>>()

    override val simpleName: String
        get() = functionName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

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
        declaredPins.add(DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeFunctionInvoke>()
                    .first().dependencies[parameterName] = node
            },
            lazyName = { parameterName },
            lazyType = { type }
        ))
        beginType.addParameter(parameterName, type)

        beginBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    fun addOutput(resultName: String, type: VariableType<*>) {
        declaredPins.add(DeclaredSingleOutputDataPin(
            nodeFabric = { NodeFunctionReturned(resultName) },
            lazyName = { resultName },
            lazyType = { type }
        ))
        endType.addResult(resultName, type)

        beginBlock.update()
        invocationBlocks.forEach { it.update() }
    }
}