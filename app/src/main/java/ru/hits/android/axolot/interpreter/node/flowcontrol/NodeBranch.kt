package ru.hits.android.axolot.interpreter.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable

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
        throw UnsupportedOperationException("Не поддерживается")
    }
}