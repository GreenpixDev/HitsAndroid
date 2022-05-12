package ru.hits.android.axolot.interpreter.service.impl.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService

class NodeFunctionEndService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeFunctionEnd) {
            // Записываем все входные переменные функции в стек
            node.dependencies.forEach {
                val key = context.stack.peek().invocable to it.key
                context.stack.peek(1).variables[key] = nodeHandlerService.invoke(it.value, context)
            }
            return null
        }
        throw createIllegalException(node)
    }

}