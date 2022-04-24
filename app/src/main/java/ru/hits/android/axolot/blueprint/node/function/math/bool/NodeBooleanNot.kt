package ru.hits.android.axolot.blueprint.node.function.math.bool

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.sin

class NodeBooleanNot : NodeFunction() {

    companion object {
        const val INPUT = "input"
    }

    fun init(input: NodeDependency) {
        dependencies[INPUT] = input
    }

    override operator fun invoke(context: Context): Variable {
        val input = dependencies[INPUT]!!.invoke(context)[Type.BOOLEAN]!!
        return Variable(Type.BOOLEAN, !input)
    }
}