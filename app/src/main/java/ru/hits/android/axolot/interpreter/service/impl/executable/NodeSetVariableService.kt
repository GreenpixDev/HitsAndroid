package ru.hits.android.axolot.interpreter.service.impl.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.NodeSetVariable
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService

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