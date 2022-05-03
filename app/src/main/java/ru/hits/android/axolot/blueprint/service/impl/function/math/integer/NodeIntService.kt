package ru.hits.android.axolot.blueprint.service.impl.function.math.integer

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.integer.*
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class NodeIntService: NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeIntAbs) {
            val input = node.dependencies[NodeIntAbs.INPUT]!!.invoke(context)[Type.INT]
            val result = input?.let { abs(it) }
            return Variable(Type.FLOAT, result)
        }

        else if(node is NodeIntDiv) {
            var sum = 0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]
                input?.let { sum /= input }
            }
            return Variable(Type.INT, sum)
        }

        else if(node is NodeIntEqual) {
            val first = node.dependencies[NodeIntEqual.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntEqual.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first == second)
        }

        else if(node is NodeIntLess) {
            val first = node.dependencies[NodeIntLess.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntLess.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first < second)
        }

        else if(node is NodeIntLessOrEqual) {
            val first = node.dependencies[NodeIntLessOrEqual.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntLessOrEqual.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first <= second)
        }

        else if(node is NodeIntMax) {
            var max = Integer.MIN_VALUE
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]!!
                max = max(max, input)
            }
            return Variable(Type.INT, max)
        }

        else if(node is NodeIntMin) {
            var min = Integer.MAX_VALUE
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]!!
                min = min(min, input)
            }
            return Variable(Type.INT, min)
        }

        else if(node is NodeIntMod) {
            var sum = 0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]
                input?.let { sum %= input }
            }
            return Variable(Type.INT, sum)
        }

        else if(node is NodeIntMore) {
            val first = node.dependencies[NodeIntMore.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntMore.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first > second)
        }

        else if(node is NodeIntMoreOrEqual) {
            val first = node.dependencies[NodeIntMoreOrEqual.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntMoreOrEqual.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first >= second)
        }

        else if(node is NodeIntMul) {
            var sum = 0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]
                input?.let { sum *= input }
            }
            return Variable(Type.INT, sum)
        }

        else if(node is NodeIntNotEqual) {
            val first = node.dependencies[NodeIntNotEqual.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntNotEqual.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.BOOLEAN, first != second)
        }

        else if(node is NodeIntSub) {
            val first = node.dependencies[NodeIntSub.FIRST]!!.invoke(context)[Type.INT]!!
            val second = node.dependencies[NodeIntSub.SECOND]!!.invoke(context)[Type.INT]!!
            return Variable(Type.INT, first - second)
        }

        else if(node is NodeIntSum) {
            var sum = 0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.INT]
                input?.let { sum += input }
            }
            return Variable(Type.INT, sum)
        }
        throw createIllegalException(node)
    }

}