package ru.hits.android.axolot.interpreter.service.impl.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeForLoopBreak
import ru.hits.android.axolot.interpreter.service.NodeExecutableService

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopBreakService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeForLoopBreak) {
            context.stack[node]?.value = true
            return null
        }
        throw createIllegalException(node)
    }

}