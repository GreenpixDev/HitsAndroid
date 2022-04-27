package ru.hits.android.axolot.executor.node.service.impl

import ru.hits.android.axolot.executor.node.Node
import ru.hits.android.axolot.executor.node.NodeExecutable
import ru.hits.android.axolot.executor.node.entity.NodePrintln
import ru.hits.android.axolot.executor.node.service.NodeExecutableService
import ru.hits.android.axolot.executor.node.service.NodeHandlerService

class NodePrintlnService(private val nodeHandlerService: NodeHandlerService) : NodeExecutableService {

    override fun invoke(node: Node): NodeExecutable? {
        if(node is NodePrintln) {
            val variable = nodeHandlerService.invoke(node.dependencies[NodePrintln.PARAM])
            println(variable.value.toString())
            return node.nextNode
        }
        throw IllegalArgumentException("This is not NodePrintln")
    }

}