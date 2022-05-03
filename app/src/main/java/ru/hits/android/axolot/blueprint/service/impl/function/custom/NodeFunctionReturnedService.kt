package ru.hits.android.axolot.blueprint.service.impl.function.custom

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeFunctionReturnedService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeFunctionReturned) {
            return context.stack[node.nodeInvoke to node.name]!!
        }
        throw createIllegalException(node)
    }

}