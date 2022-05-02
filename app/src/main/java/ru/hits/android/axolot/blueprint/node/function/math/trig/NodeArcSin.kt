package ru.hits.android.axolot.blueprint.node.function.math.trig

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.asin

class NodeArcSin : NodeFunction() {

    companion object {
        const val INPUT = 0
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val input = dependencies[INPUT].invoke(context)[Type.FLOAT]
        val result = input?.let { asin(it) }
        return Variable(Type.FLOAT, result)
    }
}