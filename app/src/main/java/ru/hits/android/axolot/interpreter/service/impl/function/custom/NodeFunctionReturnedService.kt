package ru.hits.android.axolot.interpreter.service.impl.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFunctionReturnedService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeFunctionReturned) {
            val returned = context.stack[node.nodeInvoke to node.name]
            requireNotNull(returned) { "Функция не вернула требуемый параметр ${node.name}" }
            return returned
        }
        throw createIllegalException(node)
    }

}