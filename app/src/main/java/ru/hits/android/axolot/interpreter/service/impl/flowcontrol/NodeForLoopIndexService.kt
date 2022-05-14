package ru.hits.android.axolot.interpreter.service.impl.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeForLoopIndex
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.variable.Variable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopIndexService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeForLoopIndex) {
            return context.stack[node]!!
        }
        throw createIllegalException(node)
    }

}