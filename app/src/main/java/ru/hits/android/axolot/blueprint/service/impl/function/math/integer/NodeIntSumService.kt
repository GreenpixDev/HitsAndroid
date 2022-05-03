package ru.hits.android.axolot.blueprint.service.impl.function.math.integer

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntEqual
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntSum
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeIntSumService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeIntSum){
            var sum = 0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]
                input?.let { sum += input }
            }
            return Variable(Type.INT, sum)
        }
        throw createIllegalException(node)
    }

}