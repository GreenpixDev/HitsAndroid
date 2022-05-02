package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoop : NodeExecutable() {

    var loopBody: NodeExecutable? = null

    var completed: NodeExecutable? = null

    companion object {
        const val FIRST_INDEX = 0
        const val LAST_INDEX = 1
    }

    fun init(firstIndex: NodeDependency, lastIndex: NodeDependency) {
        dependencies[FIRST_INDEX] = firstIndex
        dependencies[LAST_INDEX] = lastIndex
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val firstIndex = dependencies[FIRST_INDEX].invoke(context)[Type.INT]!!
        val lastIndex = dependencies[LAST_INDEX].invoke(context)[Type.INT]!!

        // Добавляем в стек переменные для цикла
        val idxBreak = context.stack.peek().add(Variable(Type.BOOLEAN, false))
        val idxIndex = context.stack.peek().add(Variable(Type.INT, 0))

        // Цикл
        for (i in firstIndex..lastIndex) {
            // Задаем индекс и выполняем итерацию
            context.stack.peek()[idxIndex].value = i
            context.interpreter.execute(loopBody)

            // Если цикл остановлен - завершаем его
            if (context.stack.peek()[idxBreak].value as Boolean) {
                break
            }
        }

        // Удаляем переменные из стека
        context.stack.peek().removeAt(idxBreak)
        context.stack.peek().removeAt(idxIndex)

        return completed
    }
}