package ru.hits.android.axolot.interpreter.service.impl.executable.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type

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