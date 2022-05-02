package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type

@Deprecated("Этот узел можно сделать не нативным")
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
        val firstIndex = dependencies[FIRST_INDEX]!!.invoke(context)[Type.INT]!!
        val lastIndex = dependencies[LAST_INDEX]!!.invoke(context)[Type.INT]!!

        // Локальный контекст для индексов и остановки цикла
        context.createLocalContext(this, mapOf(INDEX to 0, BREAK to false)).use {
            // Цикл
            for (i in firstIndex..lastIndex) {
                // Задаем индекс и выполняем итерацию
                it[INDEX] = i
                context.interpreter.execute(loopBody, context.createChild())

                // Если цикл остановлен - завершаем его
                if (it[BREAK] as Boolean) {
                    break
                }
            }

        }
        return completed
    }
}