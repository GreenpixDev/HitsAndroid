package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeBranch : NodeExecutable() {

    var trueNode: NodeExecutable? = null

    var falseNode: NodeExecutable? = null

    companion object {
        const val CONDITION = 0
    }

    fun init(input: NodeDependency) {
        dependencies[CONDITION] = input
    }

    override fun invoke(context: Context): NodeExecutable? {
        val condition = context.params[CONDITION]!![Type.BOOLEAN]!!
        return if (condition) trueNode else falseNode
    }
}