package ru.hits.android.axolot.blueprint.node.function.custom

import ru.hits.android.axolot.blueprint.element.BlueprintFunction
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeFunctionEnd(val function: BlueprintFunction) : NodeExecutable() {

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        // Записываем все входные переменные функции в стек
        dependencies.forEach {
            val key = context.stack.peek().invocable to it.key
            context.stack.peek(1).variables[key] = it.value.invoke(context)
        }

        return null
    }
}