package ru.hits.android.axolot.interpreter.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFloatMul : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    fun add(input: NodeDependency) {
        dependencies[dependencies.size] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var mul = 0.0
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.FLOAT]
            input?.let { mul *= input }
        }
        return Variable(Type.FLOAT, mul)
    }
}