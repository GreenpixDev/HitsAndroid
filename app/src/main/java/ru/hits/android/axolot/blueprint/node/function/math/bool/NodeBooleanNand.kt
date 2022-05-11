package ru.hits.android.axolot.blueprint.node.function.math.bool

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.node.function.math.real.NodeFloatSub
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeBooleanNand : NodeFunction() {

    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }

    fun init(first: NodeDependency, second: NodeDependency) {
        dependencies[NodeFloatSub.FIRST] = first
        dependencies[NodeFloatSub.SECOND] = second
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val first = dependencies[NodeBooleanNand.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
        val second = dependencies[NodeBooleanNand.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
        return Variable(Type.BOOLEAN, if (first) !second else true)
    }

}