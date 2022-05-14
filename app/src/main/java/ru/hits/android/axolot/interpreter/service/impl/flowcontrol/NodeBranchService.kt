package ru.hits.android.axolot.interpreter.service.impl.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type

class NodeBranchService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeBranch) {
            val condition = nodeHandlerService.invoke(
                node[NodeBranch.CONDITION],
                context
            )[Type.BOOLEAN]!!
            return if (condition) node.trueNode else node.falseNode
        }
        throw createIllegalException(node)
    }

}