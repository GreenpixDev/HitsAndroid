package ru.hits.android.axolot.blueprint.service.impl.function.math.integer

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntSub
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeIntSubService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeIntSub){
            val first = node.dependencies[NodeIntSub.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntSub.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.INT, first - second)
        }
        throw createIllegalException(node)
    }

}