package ru.hits.android.axolot.interpreter.service.impl.function.math

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.math.bool.*
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeBooleanService(private val nodeHandlerService: NodeHandlerService) :
    NodeDependencyService {
    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeBooleanAnd) {
            // TODO (переделать)
        } else if (node is NodeBooleanNand) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanNand.FIRST]!!,
                context
            )[Type.BOOLEAN]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanNand.SECOND]!!,
                context
            )[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, if (first) !second else true)
        }
        else if(node is NodeBooleanNor) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanNor.FIRST]!!,
                context
            )[Type.BOOLEAN]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanNor.SECOND]!!,
                context
            )[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, !(first || second))
        }
        else if(node is NodeBooleanNot) {
            val input = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanNot.INPUT]!!,
                context
            )[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, !input)
        }
        else if(node is NodeBooleanOr) {
            // TODO (переделать)
        }
        else if(node is NodeBooleanXnor) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanXnor.FIRST]!!,
                context
            )[Type.BOOLEAN]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanXnor.SECOND]!!,
                context
            )[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, (first == second))
        }
        else if(node is NodeBooleanXor) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanXor.FIRST]!!,
                context
            )[Type.BOOLEAN]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeBooleanXor.SECOND]!!,
                context
            )[Type.BOOLEAN]!!
            return Variable(Type.BOOLEAN, first.xor(second))
        }
        throw createIllegalException(node)
    }
}