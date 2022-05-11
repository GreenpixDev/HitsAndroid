package ru.hits.android.axolot.blueprint.service.impl.flowcontrol

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoopIndex
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopIndexService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeForLoopIndex) {
            return context.stack[node]!!
        }
        throw createIllegalException(node)
    }

}