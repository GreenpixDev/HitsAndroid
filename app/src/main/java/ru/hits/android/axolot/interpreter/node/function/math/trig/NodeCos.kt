package ru.hits.android.axolot.interpreter.node.function.math.trig

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.cos

class NodeCos : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT]!!.invoke(context)[Type.FLOAT]
        val result = input?.let { cos(it) }
        return Variable(Type.FLOAT, result)
    }
}