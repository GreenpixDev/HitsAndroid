package ru.hits.android.axolot.blueprint.service.impl.executable.array

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeArrayAssignElementService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeArrayAssignElement) {
            val array = nodeHandlerService.invoke(
                node.dependencies[NodeArrayAssignElement.ARRAY]!!,
                context
            ).getArray()!!
            val index = nodeHandlerService.invoke(
                node.dependencies[NodeArrayAssignElement.INDEX]!!,
                context
            )[Type.INT]!!
            val value = nodeHandlerService.invoke(
                node.dependencies[NodeArrayAssignElement.VALUE]!!,
                context
            )

            array[index] = value

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}