package ru.hits.android.axolot.blueprint.service.impl.function.array

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.array.NodeArrayFindElement
import ru.hits.android.axolot.blueprint.node.function.array.NodeArrayGetElement
import ru.hits.android.axolot.blueprint.node.function.array.NodeArraySize
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext


class NodeArrayService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeArrayFindElement) {
            val array =
                nodeHandlerService.invoke(node[NodeArrayFindElement.ARRAY], context)
                    .getArray()!!
            val element = nodeHandlerService.invoke(
                node[NodeArrayFindElement.ELEMENT],
                context
            ).value

            return Variable(Type.INT, array.find { it.value == element })
        } else if (node is NodeArrayGetElement) {
            val array =
                nodeHandlerService.invoke(node[NodeArrayGetElement.ARRAY], context)
                    .getArray()!!
            val index = nodeHandlerService.invoke(
                node[NodeArrayGetElement.INDEX],
                context
            )[Type.INT]!!

            return array[index]
        }
        else if(node is NodeArraySize){
            val array = nodeHandlerService.invoke(node[NodeArraySize.ARRAY], context)
                .getArray()!!

            return Variable(Type.INT, array.size)
        }
        throw createIllegalException(node)
    }

}