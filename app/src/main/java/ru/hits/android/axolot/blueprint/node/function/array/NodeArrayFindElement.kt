package ru.hits.android.axolot.blueprint.node.function.array

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeArrayFindElement : NodeFunction() {

    companion object {
        const val ARRAY = 0
        const val ELEMENT = 1
    }

    fun init(array: NodeDependency, element: NodeDependency) {
        dependencies[ARRAY] = array
        dependencies[ELEMENT] = element
    }

    override fun invoke(context: InterpreterContext): Variable {
        val array = dependencies[ARRAY].invoke(context).getArray()!!
        val element = dependencies[ELEMENT].invoke(context).value

        return Variable(Type.INT, array.find { it.value == element })
    }
}