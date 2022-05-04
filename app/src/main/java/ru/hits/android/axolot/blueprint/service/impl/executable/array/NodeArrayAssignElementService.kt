package ru.hits.android.axolot.blueprint.service.impl.executable.array

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeArrayAssignElementService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeArrayAssignElement) {
            val array = node.dependencies[NodeArrayAssignElement.ARRAY]!!.invoke(context).getArray()!!
            val index = node.dependencies[NodeArrayAssignElement.INDEX]!!.invoke(context)[Type.INT]!!
            val value = node.dependencies[NodeArrayAssignElement.VALUE]!!.invoke(context)

            array[index] = value

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}