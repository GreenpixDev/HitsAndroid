package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.*
import ru.hits.android.axolot.blueprint.element.AxolotBaseSource
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.util.getThemeColor

/**
 * Декларация макроса
 */
class MacrosType(
    var macrosName: String
) : AxolotBaseSource(), BlockType {

    companion object {
        const val PREFIX_NAME = "macros"
        private var counter = 0L
    }

    lateinit var beginBlock: AxolotBlock
    lateinit var endBlock: AxolotBlock
    val invocationBlocks = mutableListOf<AxolotBlock>()

    val beginType = MacrosBeginType(this)
    val endType = MacrosEndType(this)

    private val inputNames = mutableMapOf<Long, String>()
    private val outputNames = mutableMapOf<Long, String>()
    private val inputTypes = mutableMapOf<Long, VariableType<*>>()
    private val outputTypes = mutableMapOf<Long, VariableType<*>>()

    override val simpleName: String
        get() = macrosName

    override val fullName: String
        get() = "$PREFIX_NAME.$simpleName"

    override val declaredPins = mutableListOf<DeclaredPin>()

    override fun getDisplayName(context: Context): String {
        return macrosName
    }

    override fun createBlock(): AxolotBlock {
        val block = super<BlockType>.createBlock()
        invocationBlocks.add(block)
        return block
    }

    fun addInputFlow(inputName: String) {
        val pin = DeclaredSingleInputFlowPin(
            nodeFabric = { NodeMacrosInput(inputName) },
            lazyName = { inputName }
        )
        declaredPins.add(pin)
        beginType.addFlow(inputName)

        beginBlock.update()
        endBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    fun addInputData(inputName: String, type: VariableType<*>) {
        val identifier = counter++
        val pin = DeclaredSingleInputDataPin(
            handler = { target, node ->
                target
                    .filterIsInstance<NodeMacrosDependency>()
                    .find { it.name == inputName }?.init(node)
            },
            lazyName = { inputName },
            lazyType = { type }
        )
        declaredPins.add(pin)
        beginType.addData(inputName, type)

        beginBlock.update()
        endBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    fun addOutputFlow(outputName: String) {
        val identifier = counter++
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
        endType.addFlow(outputName)

        beginBlock.update()
        endBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    fun addOutputData(outputName: String, type: VariableType<*>) {
        val identifier = counter++
        val pin = DeclaredSingleOutputDataPin(
            nodeFabric = { NodeMacrosDependency(outputName) },
            lazyName = { outputName },
            lazyType = { type }
        )
        declaredPins.add(pin)
        endType.addData(outputName, type)

        beginBlock.update()
        endBlock.update()
        invocationBlocks.forEach { it.update() }
    }

    override fun getDisplayColor(context: Context): Int {
        return context.getThemeColor(R.attr.colorBlockHeaderMacros)
    }

}