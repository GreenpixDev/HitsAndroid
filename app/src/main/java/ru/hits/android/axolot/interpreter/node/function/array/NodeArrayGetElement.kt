package ru.hits.android.axolot.interpreter.node.function.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeArrayGetElement : NodeFunction() {

    companion object {
        const val ARRAY = 0
        const val INDEX = 1
    }

    fun init(array: NodeDependency, index: NodeDependency) {
        dependencies[ARRAY] = array
        dependencies[INDEX] = index
    }

    override fun invoke(context: InterpreterContext): Variable {
        val array = dependencies[ARRAY]!!.invoke(context).getArray()!!
        val index = dependencies[INDEX]!!.invoke(context)[Type.INT]!!

        return array[index]
    }
}