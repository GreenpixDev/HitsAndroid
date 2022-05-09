package ru.hits.android.axolot.interpreter.node.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.element.InterpretedFunction
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.stack.StackFrame

class NodeFunctionInvoke(val function: InterpretedFunction) : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        // Получаем все входные переменные функции
        val frame = StackFrame(this)
        dependencies.forEach {
            frame.variables[function.input[it.key]!!] = it.value.invoke(context)
        }

        // Увеличиваем стек (вход в функцию)
        context.stack.push(frame)

        // Выполняем функцию
        context.interpreter.execute(function.inputExecutable)

        // Уменьшаем стек (выход из функции)
        context.stack.pop()

        return nextNode
    }
}