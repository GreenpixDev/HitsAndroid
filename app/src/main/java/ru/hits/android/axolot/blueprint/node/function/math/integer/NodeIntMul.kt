package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeIntMul : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        dependencies.addAll(input)
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var sum = 1
        for (i in dependencies.indices) {
            val input = dependencies[i].invoke(context)[Type.INT]
            input?.let { sum *= input }
        }
        return Variable(Type.INT, sum)
    }
}