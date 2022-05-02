package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeMacrosDependency : NodeFunction() {

    companion object {
        const val INPUT = 0
    }

    fun init(node: NodeDependency) {
        dependencies[INPUT] = node
    }

    override fun invoke(context: InterpreterContext): Variable {
        return dependencies[INPUT].invoke(context)
    }
}