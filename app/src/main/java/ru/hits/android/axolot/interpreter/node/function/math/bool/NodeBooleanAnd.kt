package ru.hits.android.axolot.interpreter.node.function.math.bool

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.node.function.math.real.NodeFloatSub
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeBooleanAnd : NodeFunction() {

    companion object {
        const val FIRST = 0
        const val SECOND = 1
    }

    fun init(first: NodeDependency, second: NodeDependency) {
        dependencies[NodeFloatSub.FIRST] = first
        dependencies[NodeFloatSub.SECOND] = second
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val first = dependencies[NodeBooleanNor.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
        val second = dependencies[NodeBooleanNor.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
        return Variable(Type.BOOLEAN, first && second)
    }
}