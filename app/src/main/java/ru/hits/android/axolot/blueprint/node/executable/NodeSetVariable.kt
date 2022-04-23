package ru.hits.android.axolot.blueprint.node.executable

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeSetVariable(val name: String) : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val INPUT = 0
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: Context): NodeExecutable? {
        val value = context.params[INPUT]!!
        context.scope.setVariable(name, value)
        return nextNode
    }
}