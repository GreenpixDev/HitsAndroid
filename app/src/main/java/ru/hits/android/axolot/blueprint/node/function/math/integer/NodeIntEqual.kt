package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.math.log

class NodeIntEqual : NodeFunction() {

    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }

    fun init(first: NodeDependency, second: NodeDependency) {
        dependencies[FIRST] = first
        dependencies[SECOND] = second
    }

    override operator fun invoke(context: Context): Variable {
        val first = dependencies[FIRST]!!.invoke(context)[Type.INT]!!
        val second = dependencies[SECOND]!!.invoke(context)[Type.INT]!!
        return Variable(Type.BOOLEAN, first == second)
    }
}