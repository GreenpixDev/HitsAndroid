package ru.hits.android.axolot.interpreter.service.impl.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeWhileLoop
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type

@Deprecated("Этот узел можно сделать не нативным")
class NodeWhileLoopService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeWhileLoop) {
            val condition = nodeHandlerService.invoke(
                node[NodeWhileLoop.CONDITION],
                context
            )[Type.BOOLEAN]!!

            // Цикл
            while (condition) {
                // Выполняем итерацию
                context.interpreter.execute(node.loopBody)
            }

            return node.completed
        }
        throw createIllegalException(node)
    }

}