package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.service.ServiceInit
import ru.hits.android.axolot.blueprint.stack.Stack
import ru.hits.android.axolot.util.SuppliedThreadLocal

class BlueprintInterpreter(val scope: Scope) : Interpreter {

    val nodeHandlerService = NodeHandlerService()

    val stack = SuppliedThreadLocal { Stack() }

    init {
        val serviceInit = ServiceInit(nodeHandlerService)
        nodeHandlerService.init(serviceInit.intiHandler())
    }

    override fun execute(node: NodeExecutable?) {
        var currentNode = node

        while (currentNode != null) {
            // Выполняем функцию и получаем следующую ноду
            val nextNode = nodeHandlerService.invoke(currentNode, BlueprintInterpreterContext(this))
            // Далее
            currentNode = nextNode
        }
    }
}