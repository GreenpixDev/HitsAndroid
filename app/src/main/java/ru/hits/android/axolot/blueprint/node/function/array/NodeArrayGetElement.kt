package ru.hits.android.axolot.blueprint.node.function.array

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

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