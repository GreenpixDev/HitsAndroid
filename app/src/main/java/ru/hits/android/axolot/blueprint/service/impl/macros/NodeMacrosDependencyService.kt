package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeMacrosDependencyService(private val nodeHandlerService: NodeHandlerService) :
    NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeMacrosDependency) {
            return nodeHandlerService.invoke(
                node.dependencies[NodeMacrosDependency.INPUT]!!,
                context
            )
        }
        throw createIllegalException(node)
    }

}