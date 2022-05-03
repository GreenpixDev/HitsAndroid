package ru.hits.android.axolot.blueprint.service.impl.function

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeCastService :NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeCast){
            val input = node.dependencies[NodeCast.INPUT]!!.invoke(context)
            return Variable(node.cast, node.cast.cast(input))
        }
        throw createIllegalException(node)
    }

}