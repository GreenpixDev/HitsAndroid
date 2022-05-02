package ru.hits.android.axolot.blueprint.node.function.custom

import ru.hits.android.axolot.blueprint.element.BlueprintFunction
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.stack.StackFrame
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeFunctionInvoke(val function: BlueprintFunction) : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        // Получаем все входные переменные функции
        val params = hashMapOf<Any, Variable>()
        dependencies.forEach {
            params[function.input[it.key]!!] = it.value.invoke(context)
        }

        // Увеличиваем стек (вход в функцию)
        context.stack.push(StackFrame(this))
        context.stack.peek().variables.putAll(params)

        // Выполняем функцию
        context.interpreter.execute(function.inputExecutable)

        // Уменьшаем стек (выход из функции)
        context.stack.pop()

        return nextNode
    }
}