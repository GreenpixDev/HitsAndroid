package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeMacrosDependencyService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeMacrosDependency){
            return node.dependencies[NodeMacrosDependency.INPUT]!!.invoke(context)
        }
        throw createIllegalException(node)
    }

}