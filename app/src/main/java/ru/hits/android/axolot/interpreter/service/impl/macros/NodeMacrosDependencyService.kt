package ru.hits.android.axolot.interpreter.service.impl.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeMacrosDependencyService(private val nodeHandlerService: NodeHandlerService) :
    NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeMacrosDependency) {
            return nodeHandlerService.invoke(
                node[NodeMacrosDependency.INPUT],
                context
            )
        }
        throw createIllegalException(node)
    }

}