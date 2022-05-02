package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type

@Deprecated("Этот узел можно сделать не нативным")
class NodeWhileLoop : NodeExecutable() {

    var loopBody: NodeExecutable? = null

    var completed: NodeExecutable? = null

    companion object {
        const val CONDITION = 0
    }

    fun init(condition: NodeDependency) {
        dependencies[CONDITION] = condition
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val condition = dependencies[CONDITION]!!.invoke(context)[Type.BOOLEAN]!!

        // Цикл
        while (condition) {
            // Выполняем итерацию
            context.interpreter.execute(loopBody)
        }

        return completed
    }
}