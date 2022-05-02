package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeSequence : NodeExecutable() {

    private val nextNodes: MutableList<NodeExecutable> = mutableListOf()

    fun then(then: NodeExecutable) {
        nextNodes.add(then)
    }

    override fun invoke(context: Context): NodeExecutable {
        for (i in 0..nextNodes.size - 2) {
            context.interpreter.execute(nextNodes[i], context.createChild())
        }
        return nextNodes.last()
    }
}