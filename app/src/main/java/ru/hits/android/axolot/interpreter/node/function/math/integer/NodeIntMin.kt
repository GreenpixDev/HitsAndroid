package ru.hits.android.axolot.interpreter.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.min

class NodeIntMin : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    fun add(input: NodeDependency) {
        dependencies[dependencies.size] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var min = Integer.MAX_VALUE
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.INT]!!
            min = min(min, input)
        }
        return Variable(Type.INT, min)
    }
}