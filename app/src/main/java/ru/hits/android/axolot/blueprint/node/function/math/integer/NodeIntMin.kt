package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.min

class NodeIntMin : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        dependencies.addAll(input)
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        var min = Integer.MAX_VALUE
        for (i in dependencies.indices) {
            val input = dependencies[i].invoke(context)[Type.INT]!!
            min = min(min, input)
        }
        return Variable(Type.INT, min)
    }
}