package ru.hits.android.axolot.blueprint.service.impl.function

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeCastService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeCast) {
            val input = nodeHandlerService.invoke(node.dependencies[NodeCast.INPUT]!!, context);
            return Variable(node.cast, node.cast.cast(input))
        }
        throw createIllegalException(node)
    }

}