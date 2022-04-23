package ru.hits.android.axolot.blueprint.node.function

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeCast(val cast: VariableType<*>) : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: Context): Variable {
        val input = context.params[INPUT]!!
        return Variable(cast, cast.cast(input))
    }
}