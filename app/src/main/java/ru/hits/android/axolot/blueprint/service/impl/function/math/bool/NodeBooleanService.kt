package ru.hits.android.axolot.blueprint.service.impl.function.math.bool

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.bool.*
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeBooleanService : NodeDependencyService {
    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeBooleanAnd) {
            var result = true
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.BOOLEAN]!!
                result = result && input
            }
            return Variable(Type.BOOLEAN, result)
        }
        else if(node is NodeBooleanNand) {
            val first = node.dependencies[NodeBooleanNand.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
            val second = node.dependencies[NodeBooleanNand.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, if (first) !second else true)
        }
        else if(node is NodeBooleanNor) {
            val first = node.dependencies[NodeBooleanNor.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
            val second = node.dependencies[NodeBooleanNor.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, !(first || second))
        }
        else if(node is NodeBooleanNot) {
            val input = node.dependencies[NodeBooleanNot.INPUT]!!.invoke(context)[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, !input)
        }
        else if(node is NodeBooleanOr) {
            var result = false
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.BOOLEAN]!!
                result = result || input
            }
            return Variable(Type.BOOLEAN, result)
        }
        else if(node is NodeBooleanXnor) {
            val first = node.dependencies[NodeBooleanXnor.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
            val second = node.dependencies[NodeBooleanXnor.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, (first == second))
        }
        else if(node is NodeBooleanXor) {
            val first = node.dependencies[NodeBooleanXor.FIRST]!!.invoke(context)[Type.BOOLEAN]!!
            val second = node.dependencies[NodeBooleanXor.SECOND]!!.invoke(context)[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, first.xor(second))
        }
        throw createIllegalException(node)
    }
}