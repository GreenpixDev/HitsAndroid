package ru.hits.android.axolot.interpreter.service.impl.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFunctionParameterService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeFunctionParameter) {
            return context.stack[node]!!
        }
        throw createIllegalException(node)
    }

}