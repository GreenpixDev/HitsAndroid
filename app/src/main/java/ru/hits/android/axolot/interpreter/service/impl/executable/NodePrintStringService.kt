package ru.hits.android.axolot.interpreter.service.impl.executable

import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type

class NodePrintStringService(private val nodeHandlerService: NodeHandlerService, val console: Console) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodePrintString) {
            val string = nodeHandlerService.invoke(
                node[NodePrintString.STRING],
                context
            )[Type.STRING]
            console.sendStringToUser(string)
            println(string)
            return node.nextNode
        }
        throw createIllegalException(node)
    }

}