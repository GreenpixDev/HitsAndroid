package ru.hits.android.axolot.blueprint.node.function.math.bool

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeBooleanAnd : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var result = true
        for (i in dependencies.values.indices) {
            val input = dependencies[i]!!.invoke(context)[Type.BOOLEAN]!!
            result = result && input
        }
        return Variable(Type.BOOLEAN, result)
    }
}