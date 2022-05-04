package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosOutput
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeMacrosOutputService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeMacrosOutput){
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}