package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type

class NodeBranch : NodeExecutable() {

    var trueNode: NodeExecutable? = null

    var falseNode: NodeExecutable? = null

    companion object {
        const val CONDITION = 0
    }

    fun init(input: NodeDependency) {
        dependencies[CONDITION] = input
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val condition = dependencies[CONDITION].invoke(context)[Type.BOOLEAN]!!
        return if (condition) trueNode else falseNode
    }
}