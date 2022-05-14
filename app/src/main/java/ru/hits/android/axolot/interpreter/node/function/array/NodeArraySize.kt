package ru.hits.android.axolot.interpreter.node.function.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeArraySize : NodeFunction() {

    companion object {
        const val ARRAY = 0
    }

    fun init(array: NodeDependency) {
        dependencies[ARRAY] = array
    }

    override fun invoke(context: InterpreterContext): Variable {
        val array = dependencies[ARRAY]!!.invoke(context).getArray()!!

        return Variable(Type.INT, array.size)
    }
}