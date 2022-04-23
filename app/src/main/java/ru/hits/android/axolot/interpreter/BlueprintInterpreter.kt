package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.context.CacheableContext
import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.context.SimpleContext
import ru.hits.android.axolot.blueprint.node.*
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.variable.Variable

class BlueprintInterpreter(override val scope: Scope) : Interpreter {

    override fun execute(node: NodeExecutable?, context: Context) {
        var currentNode = node

        while (currentNode != null) {

            // Запрашиваем необходимые параметры
            val params = currentNode.dependencies.mapValues { request(it.value, context) }

            // Отчищаем кэш (если включен)
            if (context is CacheableContext) {
                context.cache.clear()
            }

            // Выполняем функцию и получаем следующую ноду
            val nextNode = currentNode(context.createChild(params))
            context.local.remove(currentNode)

            // Далее
            currentNode = nextNode
        }
    }

    override fun createContext(cache: Boolean): Context {
        if (cache) {
            return CacheableContext(this, emptyMap(), mutableMapOf(), mutableMapOf())
        }
        return SimpleContext(this, emptyMap(), mutableMapOf())
    }

    private fun request(node: NodeDependency, context: Context): Variable {
        // Если зависимость является константой
        if (node is NodeConstant) {
            return node.variable
        }

        // Если зависимость является функцией
        if (node is NodeFunction) {
            // Если есть в кэше
            if (context is CacheableContext && node in context.cache) {
                return context.cache[node]!!
            }

            // Получаем параметры (зависимости)
            val params = node.dependencies.mapValues { request(it.value, context) }

            // Получаем результат
            val result = node(context.createChild(params))

            // Добавляем в кэш (если включен)
            if (context is CacheableContext) {
                context.cache[node] = result
            }
            return result
        }

        // Если кто-то нашаманил с кодом
        throw IllegalArgumentException("invalid node: $node")
    }
}