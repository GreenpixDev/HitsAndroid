package ru.hits.android.axolot.interpreter.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class NodeAssignVariable : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val REFERENCE = 0
        const val VALUE = 1
    }

    fun init(reference: NodeDependency, value: NodeDependency) {
        dependencies[REFERENCE] = reference
        dependencies[VALUE] = value
    }

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        val reference = dependencies[REFERENCE]!!.invoke(context)
        val value = dependencies[VALUE]!!.invoke(context)

        require(reference.type == value.type) {
            "different types of reference and value: excepted ${reference.type}, actual ${value.type}"
        }

        reference.value = value.value

        return nextNode
    }
}