package ru.hits.android.axolot.executor.node.service.impl.math

import ru.hits.android.axolot.executor.node.Node
import ru.hits.android.axolot.executor.node.entity.math.NodePow
import ru.hits.android.axolot.executor.node.service.NodeDependencyService
import ru.hits.android.axolot.executor.node.service.NodeHandlerService
import ru.hits.android.axolot.executor.variable.Variable

class NodePowService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node): Variable<Double> {
        if(node is NodePow)
        {
            val number = nodeHandlerService.invoke(node.dependencies[NodePow.NUMBER])
            val pow = nodeHandlerService.invoke(node.dependencies[NodePow.POW])
            if(number && pow) {
                val res = pow(number.value, pow.value)
                //TODO вариабле блин...
            }
        }
        throw IllegalArgumentException("This is not NodePrintln")
    }
}