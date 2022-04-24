package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.max

class NodeIntMax : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: Context): Variable {
        var max = Integer.MIN_VALUE
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.INT]!!
            max = max(max, input)
        }
        return Variable(Type.INT, max)
    }
}