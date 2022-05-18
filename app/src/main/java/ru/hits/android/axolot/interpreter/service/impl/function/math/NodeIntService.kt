package ru.hits.android.axolot.interpreter.service.impl.function.math

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.function.math.integer.*
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class NodeIntService(private val nodeHandlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        when (node) {
            is NodeIntAbs -> {
                val input =
                    nodeHandlerService.invoke(
                        node.dependencies[NodeIntAbs.INPUT]!!,
                        context
                    )[Type.INT]
                val result = input?.let { abs(it) }
                return Variable(Type.FLOAT, result)
            }
            is NodeIntDiv -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntDiv.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntDiv.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.INT, first / second)
            }
            is NodeIntEqual -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntEqual.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntEqual.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first == second)
            }
            is NodeIntLess -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntLess.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntLess.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first < second)
            }
            is NodeIntLessOrEqual -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntLessOrEqual.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntLessOrEqual.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first <= second)
            }
            is NodeIntMax -> {
                var max = Integer.MIN_VALUE
                for (i in node.dependencies.values.indices) {
                    val input =
                        nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.INT]!!
                    max = max(max, input)
                }
                return Variable(Type.INT, max)
            }
            is NodeIntMin -> {
                var min = Integer.MAX_VALUE
                for (i in node.dependencies.values.indices) {
                    val input =
                        nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.INT]!!
                    min = min(min, input)
                }
                return Variable(Type.INT, min)
            }
            is NodeIntMod -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMod.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMod.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.INT, first % second)
            }
            is NodeIntMore -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMore.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMore.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first > second)
            }
            is NodeIntMoreOrEqual -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMoreOrEqual.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntMoreOrEqual.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first >= second)
            }
            is NodeIntMul -> {
                var sum = 0
                for (i in node.dependencies.values.indices) {
                    val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.INT]
                    input?.let { sum *= input }
                }
                return Variable(Type.INT, sum)
            }
            is NodeIntNotEqual -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntNotEqual.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntNotEqual.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.BOOLEAN, first != second)
            }
            is NodeIntSub -> {
                val first = nodeHandlerService.invoke(
                    node.dependencies[NodeIntSub.FIRST]!!,
                    context
                )[Type.INT]!!
                val second = nodeHandlerService.invoke(
                    node.dependencies[NodeIntSub.SECOND]!!,
                    context
                )[Type.INT]!!
                return Variable(Type.INT, first - second)
            }
            is NodeIntSum -> {
                var sum = 0
                for (i in node.dependencies.values.indices) {
                    val input = nodeHandlerService.invoke(node.dependencies[i]!!, context)[Type.INT]
                    input?.let { sum += input }
                }
                return Variable(Type.INT, sum)
            }
            else -> throw createIllegalException(node)
        }
    }

}