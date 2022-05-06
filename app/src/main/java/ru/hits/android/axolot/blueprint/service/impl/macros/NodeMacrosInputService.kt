package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosInput
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeMacrosInputService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeMacrosInput){
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}