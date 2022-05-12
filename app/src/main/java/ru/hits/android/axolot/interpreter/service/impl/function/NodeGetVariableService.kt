package ru.hits.android.axolot.interpreter.service.impl.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.NodeGetVariable
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeGetVariableService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeGetVariable)
        {
            return context.scope.getVariable(node.name)
        }
        throw createIllegalException(node)
    }

}