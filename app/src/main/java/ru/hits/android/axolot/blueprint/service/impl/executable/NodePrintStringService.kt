package ru.hits.android.axolot.blueprint.service.impl.executable

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.NodePrintString
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodePrintStringService:NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodePrintString) {
            val string = node.dependencies[NodePrintString.STRING]!!.invoke(context)[Type.STRING]
            println(string)
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}