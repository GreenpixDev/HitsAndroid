package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type

class NodeForLoop : NodeExecutable() {

    var loopBody: NodeExecutable? = null

    var completed: NodeExecutable? = null

    companion object {
        const val FIRST_INDEX = 0
        const val LAST_INDEX = 1

        const val INDEX = 0
        const val BREAK = 1
    }

    fun init(firstIndex: NodeDependency, lastIndex: NodeDependency) {
        dependencies[FIRST_INDEX] = firstIndex
        dependencies[LAST_INDEX] = lastIndex
    }

    override fun invoke(context: Context): NodeExecutable? {
        val firstIndex = context.params[FIRST_INDEX]!![Type.INT]!!
        val lastIndex = context.params[LAST_INDEX]!![Type.INT]!!

        // Локальный контекст для индексов и остановки цикла
        val localContext = arrayOf<Any>(0, false)
        context.local[this] = localContext

        // Цикл
        for (i in firstIndex..lastIndex) {
            // Задаем индекс и выполняем итерацию
            localContext[INDEX] = i
            context.interpreter.execute(loopBody, context.createChild(emptyMap()))

            // Если цикл остановлен - завершаем его
            if (localContext[BREAK] as Boolean) {
                break
            }
        }

        return completed
    }
}