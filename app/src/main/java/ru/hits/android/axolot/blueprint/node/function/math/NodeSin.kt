package ru.hits.android.axolot.blueprint.node.function.math

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.sin

class NodeSin : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: Context): Variable {
        val input = context.params[INPUT]!![Type.FLOAT]
        val result = input?.let { sin(it) }
        return Variable(Type.FLOAT, result)
    }
}