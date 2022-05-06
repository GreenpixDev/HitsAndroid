package ru.hits.android.axolot.blueprint.service.impl.flowcontrol

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeWhileLoop
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.interpreter.InterpreterContext

@Deprecated("Этот узел можно сделать не нативным")
class NodeWhileLoopService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeWhileLoop) {
            val condition = nodeHandlerService.invoke(
                node.dependencies[NodeWhileLoop.CONDITION]!!,
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