package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.abs

class NodeIntAbs : NodeFunction() {

    companion object {
        const val INPUT = 0
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT].invoke(context)[Type.INT]
        val result = input?.let { abs(it) }
        return Variable(Type.FLOAT, result)
    }
}