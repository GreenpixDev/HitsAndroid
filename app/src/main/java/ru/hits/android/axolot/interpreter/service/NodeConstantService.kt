package ru.hits.android.axolot.interpreter.service

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeConstant
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeConstantService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeConstant){
            return node.variable
        }
        throw createIllegalException(node)
    }

}