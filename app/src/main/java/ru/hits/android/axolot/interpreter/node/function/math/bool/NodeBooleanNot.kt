package ru.hits.android.axolot.interpreter.node.function.math.bool

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeBooleanNot : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT]!!.invoke(context)[Type.BOOLEAN]!!
        return Variable(Type.BOOLEAN, !input)
    }
}