package ru.hits.android.axolot.interpreter.node.executable.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.type.Type

class NodeArrayAssignElement : NodeExecutable()  {

    var nextNode: NodeExecutable? = null

    companion object {
        const val ARRAY = 0
        const val INDEX = 1
        const val VALUE = 2
    }

    fun init(array: NodeDependency, index: NodeDependency, value: NodeDependency) {
        dependencies[ARRAY] = array
        dependencies[INDEX] = index
        dependencies[VALUE] = value
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val array = dependencies[ARRAY]!!.invoke(context).getArray()!!
        val index = dependencies[INDEX]!!.invoke(context)[Type.INT]!!
        val value = dependencies[VALUE]!!.invoke(context)

        array[index] = value

        return nextNode
    }
}