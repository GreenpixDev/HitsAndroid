package ru.hits.android.axolot.interpreter.service.impl.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosOutput
import ru.hits.android.axolot.interpreter.service.NodeExecutableService

class NodeMacrosOutputService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeMacrosOutput){
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}