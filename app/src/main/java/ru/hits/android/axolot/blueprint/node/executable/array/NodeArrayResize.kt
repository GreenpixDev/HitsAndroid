package ru.hits.android.axolot.blueprint.node.executable.array

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.type.structure.ArrayType
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

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