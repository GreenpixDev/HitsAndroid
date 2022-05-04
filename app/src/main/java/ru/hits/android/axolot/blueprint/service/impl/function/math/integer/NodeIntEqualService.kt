package ru.hits.android.axolot.blueprint.service.impl.function.math.integer

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntEqual
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeIntEqualService : NodeDependencyService {
    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeIntEqual){
            val first = node.dependencies[NodeIntEqual.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntEqual.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first == second)
        }
        throw createIllegalException(node)
    }
}