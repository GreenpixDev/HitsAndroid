package ru.hits.android.axolot.interpreter.node.executable.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.structure.ArrayType
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeArrayResize : NodeExecutable()  {

    var nextNode: NodeExecutable? = null

    companion object {
        const val ARRAY = 0
        const val SIZE = 1
    }

    fun init(array: NodeDependency, size: NodeDependency) {
        dependencies[ARRAY] = array
        dependencies[SIZE] = size
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val arrayReference = dependencies[ARRAY]!!.invoke(context)
        val elementType = (arrayReference.type as ArrayType).elementType
        val size = dependencies[SIZE]!!.invoke(context)[Type.INT]!!

        arrayReference.value = Array(size) { Variable.nullVariable(elementType) }

        return nextNode
    }
}