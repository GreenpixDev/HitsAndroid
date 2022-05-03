package ru.hits.android.axolot.blueprint.service.impl.function

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeGetVariableService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeGetVariable)
        {
            return context.scope.getVariable(node.name)
        }
        throw createIllegalException(node)
    }

}