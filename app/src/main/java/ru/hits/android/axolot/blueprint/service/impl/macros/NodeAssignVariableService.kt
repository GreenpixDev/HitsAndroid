package ru.hits.android.axolot.blueprint.service.impl.macros

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.macros.NodeAssignVariable
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeAssignVariableService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeAssignVariable){
            val reference = node.dependencies[NodeAssignVariable.REFERENCE]!!.invoke(context)
            val value = node.dependencies[NodeAssignVariable.VALUE]!!.invoke(context)

            require(reference.type == value.type) {
                "different types of reference and value: excepted ${reference.type}, actual ${value.type}"
            }

            reference.value = value.value

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}