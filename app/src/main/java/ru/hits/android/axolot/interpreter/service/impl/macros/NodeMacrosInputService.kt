package ru.hits.android.axolot.interpreter.service.impl.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
import ru.hits.android.axolot.interpreter.service.NodeExecutableService

class NodeMacrosInputService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeMacrosInput){
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}