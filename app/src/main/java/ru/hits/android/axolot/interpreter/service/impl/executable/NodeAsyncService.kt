package ru.hits.android.axolot.interpreter.service.impl.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.NodeAsync
import ru.hits.android.axolot.interpreter.service.NodeExecutableService

class NodeAsyncService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeAsync) {
            Thread {
                context.interpreter.execute(node.nextNode)
            }.start()

            return null
        }

        throw createIllegalException(node)
    }
}