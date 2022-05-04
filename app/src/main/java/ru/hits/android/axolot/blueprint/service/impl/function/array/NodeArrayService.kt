package ru.hits.android.axolot.blueprint.service.impl.function.array

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.array.NodeArrayFindElement
import ru.hits.android.axolot.blueprint.node.function.array.NodeArrayGetElement
import ru.hits.android.axolot.blueprint.node.function.array.NodeArraySize
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext


class NodeArrayService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeArrayFindElement){
            val array = node.dependencies[NodeArrayFindElement.ARRAY]!!.invoke(context).getArray()!!
            val element = node.dependencies[NodeArrayFindElement.ELEMENT]!!.invoke(context).value

            return Variable(Type.INT, array.find { it.value == element })
        }
        else if(node is NodeArrayGetElement){
            val array = node.dependencies[NodeArrayGetElement.ARRAY]!!.invoke(context).getArray()!!
            val index = node.dependencies[NodeArrayGetElement.INDEX]!!.invoke(context)[Type.INT]!!

            return array[index]
        }
        else if(node is NodeArraySize){
            val array = node.dependencies[NodeArraySize.ARRAY]!!.invoke(context).getArray()!!

            return Variable(Type.INT, array.size)
        }
        throw createIllegalException(node)
    }

}