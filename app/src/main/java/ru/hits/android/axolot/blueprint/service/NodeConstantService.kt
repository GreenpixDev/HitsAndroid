package ru.hits.android.axolot.blueprint.service

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeConstant
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeConstantService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeConstant){
            return node.variable
        }
        throw createIllegalException(node)
    }

}