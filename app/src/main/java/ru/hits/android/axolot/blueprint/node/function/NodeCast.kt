package ru.hits.android.axolot.blueprint.node.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeCast(val cast: VariableType<*>) : NodeFunction() {

    companion object {
        const val INPUT = 0
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT].invoke(context)
        return Variable(cast, cast.cast(input))
    }
}