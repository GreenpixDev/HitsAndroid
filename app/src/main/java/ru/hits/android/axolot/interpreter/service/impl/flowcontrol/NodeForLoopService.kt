package ru.hits.android.axolot.interpreter.service.impl.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeForLoop
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeForLoop) {
            val firstIndex = nodeHandlerService.invoke(
                node[NodeForLoop.FIRST_INDEX],
                context
            )[Type.INT]!!
            val lastIndex = nodeHandlerService.invoke(
                node[NodeForLoop.LAST_INDEX],
                context
            )[Type.INT]!!

            // Добавляем в стек переменные для цикла
            node.nodeBreak?.let { context.stack[it] = Variable(Type.BOOLEAN, false) }
            node.nodeIndex?.let { context.stack[it] = Variable(Type.INT, 0) }

            // Цикл
            for (i in firstIndex..lastIndex) {
                // Задаем индекс и выполняем итерацию
                node.nodeIndex?.let { context.stack[it]?.value = i }
                context.interpreter.execute(node.loopBody)

                // Если цикл остановлен - завершаем его
                if (node.nodeBreak != null && context.stack[node.nodeBreak]?.value as Boolean) {
                    break
                }
            }

            // Удаляем переменную break, а index сохраняем
            node.nodeBreak?.let { context.stack[it] = null }

            return node.completed
        }
        throw createIllegalException(node)
    }

}