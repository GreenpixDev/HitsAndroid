package ru.hits.android.axolot.blueprint.node.function.math.bool

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.node.function.math.real.NodeFloatSub
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeBooleanXor : NodeFunction() {

    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }

    fun init(first: NodeDependency, second: NodeDependency) {
        dependencies[NodeFloatSub.FIRST] = first
        dependencies[NodeFloatSub.SECOND] = second
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val first = dependencies[NodeBooleanXor.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
        val second = dependencies[NodeBooleanXor.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
        return Variable(Type.BOOLEAN, first.xor(second))
    }

}