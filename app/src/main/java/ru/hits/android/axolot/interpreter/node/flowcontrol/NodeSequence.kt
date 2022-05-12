package ru.hits.android.axolot.interpreter.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class NodeSequence : NodeExecutable() {

    val nextNodes: MutableList<NodeExecutable> = mutableListOf()

    fun then(then: NodeExecutable) {
        nextNodes.add(then)
    }

    override fun invoke(context: InterpreterContext): NodeExecutable {
        for (i in 0..nextNodes.size - 2) {
            context.interpreter.execute(nextNodes[i])
        }
        return nextNodes.last()
    }
}