package ru.hits.android.axolot.blueprint.service.impl.function.custom

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeFunctionEndService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeFunctionEnd) {
            // Записываем все входные переменные функции в стек
            node.dependencies.forEach {
                val key = context.stack.peek().invocable to it.key
                context.stack.peek(1).variables[key] = it.value.invoke(context)
            }
            return null
        }
        throw createIllegalException(node)
    }

}