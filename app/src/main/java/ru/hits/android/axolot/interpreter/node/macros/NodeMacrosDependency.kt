package ru.hits.android.axolot.interpreter.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeMacrosDependency(val name: String) : NodeFunction() {

    companion object {
        const val INPUT = 0
    }

    fun init(node: NodeDependency) {
        dependencies[INPUT] = node
    }

    override fun invoke(context: InterpreterContext): Variable {
        return dependencies[INPUT]!!.invoke(context)
    }
}