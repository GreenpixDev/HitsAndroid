package ru.hits.android.axolot.interpreter.service.impl.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.macros.NodeLocalVariable
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.variable.Variable

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