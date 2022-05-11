package ru.hits.android.axolot.interpreter.service.impl.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.NodeCast
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeCastService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeCast) {
            val input = nodeHandlerService.invoke(node[NodeCast.INPUT], context);
            return Variable(node.cast, node.cast.cast(input))
        }
        throw createIllegalException(node)
    }

}