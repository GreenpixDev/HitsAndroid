package ru.hits.android.axolot.blueprint.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeSwap : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }

    fun init(firstReference: NodeDependency, secondReference: NodeDependency) {
        dependencies[FIRST] = firstReference
        dependencies[SECOND] = secondReference
    }

    override operator fun invoke(context: InterpreterContext): NodeExecutable? {
        val first = dependencies[FIRST].invoke(context)
        val second = dependencies[SECOND].invoke(context)

        val tmp = first.value
        first.value = second.value
        second.value = tmp

        return nextNode
    }
}