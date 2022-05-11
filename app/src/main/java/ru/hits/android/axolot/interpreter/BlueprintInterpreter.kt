package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.scope.Scope
import ru.hits.android.axolot.interpreter.stack.Stack
import ru.hits.android.axolot.util.SuppliedThreadLocal

class BlueprintInterpreter(val scope: Scope) : Interpreter {

    val stack = SuppliedThreadLocal { Stack() }

    override fun execute(node: NodeExecutable?) {
        var currentNode = node

        while (currentNode != null) {
            // Выполняем функцию и получаем следующую ноду
            val nextNode = currentNode(BlueprintInterpreterContext(this))

            // Далее
            currentNode = nextNode
        }
    }
}