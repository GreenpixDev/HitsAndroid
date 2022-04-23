package ru.hits.android.axolot.blueprint.node.function.math

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeFloatSum : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: Context): Variable {
        var sum = 0.0
        for (i in context.params.values.indices) {
            val input = context.params[i]!![Type.FLOAT]
            input?.let { sum += input }
        }
        return Variable(Type.FLOAT, sum)
    }
}