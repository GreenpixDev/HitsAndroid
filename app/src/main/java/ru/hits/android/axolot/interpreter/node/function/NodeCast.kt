package ru.hits.android.axolot.interpreter.node.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeCast(val cast: VariableType<*>) : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT]!!.invoke(context)
        return Variable(cast, cast.cast(input))
    }
}