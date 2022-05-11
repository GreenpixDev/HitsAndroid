package ru.hits.android.axolot.interpreter.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.min

class NodeFloatMin : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var min = Double.POSITIVE_INFINITY
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.FLOAT]!!
            min = min(min, input)
        }
        return Variable(Type.FLOAT, min)
    }
}