package ru.hits.android.axolot.interpreter.service.impl.executable.thread

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.thread.NodeSleep
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type

class NodeSleepService(private val nodeHandlerService: NodeHandlerService) : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeSleep) {
            val millis = nodeHandlerService.invoke(
                node[NodeSleep.MILLIS],
                context
            )[Type.INT]

            millis?.let { Thread.sleep(it.toLong()) }
            return node.nextNode
        }
        throw createIllegalException(node)
    }
}