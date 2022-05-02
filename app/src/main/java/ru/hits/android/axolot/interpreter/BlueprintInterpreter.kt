package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.context.SimpleContext
import ru.hits.android.axolot.blueprint.node.*
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.stack.Stack
import ru.hits.android.axolot.util.SuppliedThreadLocal

class BlueprintInterpreter(override val scope: Scope) : Interpreter {

    override val stack = SuppliedThreadLocal { Stack() }

    override fun execute(node: NodeExecutable?, context: Context) {
        var currentNode = node

        while (currentNode != null) {
            // Выполняем функцию и получаем следующую ноду
            val nextNode = currentNode(context.createChild())

            // Далее
            currentNode = nextNode
        }
    }

    override fun createContext(): Context {
        return SimpleContext(this, mutableMapOf())
    }
}