package ru.hits.android.axolot.blueprint.service.impl.flowcontrol

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoopBreak
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

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