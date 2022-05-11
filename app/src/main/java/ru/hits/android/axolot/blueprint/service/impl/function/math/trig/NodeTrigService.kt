package ru.hits.android.axolot.blueprint.service.impl.function.math.trig

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.trig.*
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext
import kotlin.math.*

class NodeTrigService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeArcCos) {
            val input = nodeHandlerService.invoke(
                node.dependencies[NodeArcCos.INPUT]!!,
                context
            )[Type.FLOAT]
            val result = input?.let { acos(it) }
            return Variable(Type.FLOAT, result)
        } else if (node is NodeArcSin) {
            val input = nodeHandlerService.invoke(
                node.dependencies[NodeArcSin.INPUT]!!,
                context
            )[Type.FLOAT]
            val result = input?.let { asin(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeArcTan){
            val input = nodeHandlerService.invoke(
                node.dependencies[NodeArcTan.INPUT]!!,
                context
            )[Type.FLOAT]
            val result = input?.let { atan(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeCos){
            val input =
                nodeHandlerService.invoke(node.dependencies[NodeCos.INPUT]!!, context)[Type.FLOAT]
            val result = input?.let { cos(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeSin){
            val input =
                nodeHandlerService.invoke(node.dependencies[NodeSin.INPUT]!!, context)[Type.FLOAT]
            val result = input?.let { sin(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeTan){
            val input =
                nodeHandlerService.invoke(node.dependencies[NodeTan.INPUT]!!, context)[Type.FLOAT]
            val result = input?.let { tan(it) }
            return Variable(Type.FLOAT, result)
        }
        throw createIllegalException(node)
    }

}