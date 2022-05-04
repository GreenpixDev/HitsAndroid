package ru.hits.android.axolot.blueprint.service.impl.flowcontrol

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeSequence
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeSequenceService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeSequence)
        {
            for (i in 0..node.nextNodes.size - 2) {
                context.interpreter.execute(node.nextNodes[i])
            }
            return node.nextNodes.last()
        }
        throw createIllegalException(node)
    }

}