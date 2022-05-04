package ru.hits.android.axolot.blueprint.service.impl.function.math.trig

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.trig.*
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext
import kotlin.math.*

class NodeTrigService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeArcCos){
            val input = node.dependencies[NodeArcCos.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { acos(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeArcSin){
            val input = node.dependencies[NodeArcSin.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { asin(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeArcTan){
            val input = node.dependencies[NodeArcTan.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { atan(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeCos){
            val input = node.dependencies[NodeCos.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { cos(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeSin){
            val input = node.dependencies[NodeSin.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { sin(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeTan){
            val input = node.dependencies[NodeTan.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { tan(it) }
            return Variable(Type.FLOAT, result)
        }
        throw createIllegalException(node)
    }

}