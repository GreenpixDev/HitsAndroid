package ru.hits.android.axolot.blueprint.node.function.array

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeArraySize : NodeFunction() {

    companion object {
        const val ARRAY = 0
    }

    fun init(array: NodeDependency) {
        dependencies[ARRAY] = array
    }

    override fun invoke(context: InterpreterContext): Variable {
        val array = dependencies[ARRAY].invoke(context).getArray()!!

        return Variable(Type.INT, array.size)
    }
}