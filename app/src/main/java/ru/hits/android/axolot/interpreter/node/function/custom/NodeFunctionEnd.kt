package ru.hits.android.axolot.interpreter.node.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.element.InterpretedFunction
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class NodeFunctionEnd(val function: InterpretedFunction) : NodeExecutable() {

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        // Записываем все входные переменные функции в стек
        dependencies.forEach {
            val key = context.stack.peek().invocable to it.key
            context.stack.peek(1).variables[key] = it.value.invoke(context)
        }

        return null
    }
}