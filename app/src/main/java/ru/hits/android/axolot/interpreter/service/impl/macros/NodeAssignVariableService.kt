package ru.hits.android.axolot.interpreter.service.impl.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.macros.NodeAssignVariable
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService

class NodeAssignVariableService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeAssignVariable) {
            val reference = nodeHandlerService.invoke(
                node[NodeAssignVariable.REFERENCE],
                context
            )
            val value = nodeHandlerService.invoke(node[NodeAssignVariable.VALUE], context)

            require(reference.type == value.type) {
                "different types of reference and value: excepted ${reference.type}, actual ${value.type}"
            }

            reference.value = value.value

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}