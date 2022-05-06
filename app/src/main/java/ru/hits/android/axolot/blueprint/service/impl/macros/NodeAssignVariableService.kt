package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.macros.NodeAssignVariable
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeAssignVariableService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeAssignVariable) {
            val reference = nodeHandlerService.invoke(
                node.dependencies[NodeAssignVariable.REFERENCE]!!,
                context
            )
            val value =
                nodeHandlerService.invoke(node.dependencies[NodeAssignVariable.VALUE]!!, context)

            require(reference.type == value.type) {
                "different types of reference and value: excepted ${reference.type}, actual ${value.type}"
            }

            reference.value = value.value

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}