package ru.hits.android.axolot.util

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeDependencyMock(val index: Int) : NodeDependency {

    override fun invoke(context: InterpreterContext): Variable {
        throw IllegalArgumentException("dependency with index $index not declared")
    }
}