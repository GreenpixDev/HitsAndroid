package ru.hits.android.axolot.blueprint.service.impl.executable

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.NodeSetVariable
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeSetVariableService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeSetVariable) {
            val value = node.dependencies[NodeSetVariable.INPUT]!!.invoke(context)
            context.scope.setVariable(node.name, value)
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}