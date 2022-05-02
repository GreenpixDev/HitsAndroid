package ru.hits.android.axolot.blueprint.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeFloatMul : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        dependencies.addAll(input)
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var sum = 1.0
        for (i in dependencies.indices) {
            val input = dependencies[i].invoke(context)[Type.FLOAT]
            input?.let { sum *= input }
        }
        return Variable(Type.FLOAT, sum)
    }
}