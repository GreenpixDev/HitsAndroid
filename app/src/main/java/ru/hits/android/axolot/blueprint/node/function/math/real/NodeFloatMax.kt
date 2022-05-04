package ru.hits.android.axolot.blueprint.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.max

class NodeFloatMax : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var max = Double.POSITIVE_INFINITY
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.FLOAT]!!
            max = max(max, input)
        }
        return Variable(Type.FLOAT, max)
    }
}