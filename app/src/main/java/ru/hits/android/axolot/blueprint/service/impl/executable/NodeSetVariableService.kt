package ru.hits.android.axolot.blueprint.service.impl.executable

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.NodeSetVariable
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeSetVariableService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeSetVariable) {
            val value =
                nodeHandlerService.invoke(node[NodeSetVariable.INPUT], context)
            context.scope.setVariable(node.name, value)
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}