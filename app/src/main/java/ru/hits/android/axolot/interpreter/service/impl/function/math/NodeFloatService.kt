package ru.hits.android.axolot.interpreter.service.impl.function.math

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.math.real.*
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min

class NodeFloatService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeFloatAbs) {
            val input = nodeHandlerService.invoke(
                node.dependencies[NodeFloatAbs.INPUT]!!,
                context
            )[Type.FLOAT]
            val result = input?.let { abs(it) }
            return Variable(Type.FLOAT, result)
        } else if (node is NodeFloatDiv) {
            // TODO (переделать)
        }
        else if(node is NodeFloatEqual) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatEqual.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatEqual.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first == second)
        }
        else if(node is NodeFloatLess) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatLess.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatLess.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first < second)
        }
        else if(node is NodeFloatLessOrEqual) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatLessOrEqual.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatLessOrEqual.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first <= second)
        }
        else if(node is NodeFloatMax) {
            var max = Double.POSITIVE_INFINITY
            for (i in node.dependencies.values.indices) {
                val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.FLOAT]!!
                max = max(max, input)
            }
            return Variable(Type.FLOAT, max)
        }
        else if(node is NodeFloatMin) {
            var min = Double.POSITIVE_INFINITY
            for (i in node.dependencies.values.indices) {
                val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.FLOAT]!!
                min = min(min, input)
            }
            return Variable(Type.FLOAT, min)
        }
        else if(node is NodeFloatMod) {
            // TODO (переделать)
        }
        else if(node is NodeFloatMore) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatMore.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatMore.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first > second)
        }
        else if(node is NodeFloatMoreOrEqual) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatMoreOrEqual.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatMoreOrEqual.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first >= second)
        }
        else if(node is NodeFloatMul) {
            var mul = 0.0
            for (i in node.dependencies.values.indices) {
                val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.FLOAT]
                input?.let { mul *= input }
            }
            return Variable(Type.FLOAT, mul)
        }
        else if(node is NodeFloatNotEqual) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatNotEqual.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatNotEqual.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first != second)
        }
        else if(node is NodeFloatSub) {
            val first = nodeHandlerService.invoke(
                node.dependencies[NodeFloatSub.FIRST]!!,
                context
            )[Type.FLOAT]!!
            val second = nodeHandlerService.invoke(
                node.dependencies[NodeFloatSub.SECOND]!!,
                context
            )[Type.FLOAT]!!
            return Variable(Type.FLOAT, first - second)
        }
        else if(node is NodeFloatSum) {
            var sum = 0.0
            for (i in node.dependencies.values.indices) {
                val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.FLOAT]
                input?.let { sum += input }
            }
            return Variable(Type.FLOAT, sum)
        }
        else if(node is NodeLog) {
            val number = nodeHandlerService.invoke(
                node.dependencies[NodeLog.NUMBER]!!,
                context
            )[Type.FLOAT]!!
            val base =
                nodeHandlerService.invoke(node.dependencies[NodeLog.BASE]!!, context)[Type.FLOAT]!!
            return Variable(Type.FLOAT, log(number, base))
        }
        throw createIllegalException(node)
    }

}