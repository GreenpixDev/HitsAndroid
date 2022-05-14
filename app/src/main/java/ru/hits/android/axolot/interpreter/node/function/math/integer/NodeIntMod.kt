package ru.hits.android.axolot.interpreter.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeIntMod : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var sum = 0
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.INT]
            input?.let { sum %= input }
        }
        return Variable(Type.INT, sum)
    }
}