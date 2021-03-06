package ru.hits.android.axolot.interpreter.service.impl.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.stack.StackFrame
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFunctionInvokeService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeFunctionInvoke) {
            // Получаем все входные переменные функции
            val params = hashMapOf<Any, Variable>()
            node.dependencies.forEach {
                params[node.function.input[it.key]!!] =
                    nodeHandlerService.invoke(it.value, context)//TODO а тут нужна проверка?????
            }

            // Увеличиваем стек (вход в функцию)
            context.stack.push(StackFrame(node))
            context.stack.peek().variables.putAll(params)

            // Выполняем функцию
            context.interpreter.execute(node.function.inputExecutable)

            // Уменьшаем стек (выход из функции)
            context.stack.pop()

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}