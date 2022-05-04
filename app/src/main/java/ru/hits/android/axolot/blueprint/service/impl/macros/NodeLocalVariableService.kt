package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.macros.NodeLocalVariable
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeLocalVariableService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeLocalVariable){
            if (node !in context.stack) {
                context.stack[node] = Variable.nullVariable(node.type)
            }

            return context.stack[node]!!
        }
        throw createIllegalException(node)
    }

}