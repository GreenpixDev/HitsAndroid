package ru.hits.android.axolot.blueprint.node.function.custom

import ru.hits.android.axolot.blueprint.element.BlueprintFunction
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeFunctionEnd(val function: BlueprintFunction) : NodeExecutable() {

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        // Записываем все входные переменные функции в стек
        for (i in dependencies.indices) {
            context.stack.peek(1).add(dependencies[i].invoke(context))
        }

        return null
    }
}