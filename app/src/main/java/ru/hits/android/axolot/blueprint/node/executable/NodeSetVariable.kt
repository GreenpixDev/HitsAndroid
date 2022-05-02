package ru.hits.android.axolot.blueprint.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeSetVariable(val name: String) : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val INPUT = 0
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): NodeExecutable? {
        val value = dependencies[INPUT].invoke(context)
        context.scope.setVariable(name, value)
        return nextNode
    }
}