package ru.hits.android.axolot.blueprint.service.impl.flowcontrol

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeBranchService : NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if(node is NodeBranch)
        {
            val condition = node.dependencies[NodeBranch.CONDITION]!!.invoke(context)[Type.BOOLEAN]!!
            return if (condition) node.trueNode else node.falseNode
        }
        throw createIllegalException(node)
    }

}