package ru.hits.android.axolot.blueprint.service.impl.function.math.real

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.function.math.real.*
import ru.hits.android.axolot.blueprint.service.NodeDependencyService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min

class NodeFloatService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if(node is NodeFloatAbs) {
            val input = node.dependencies[NodeFloatAbs.INPUT]!!.invoke(context)[Type.FLOAT]
            val result = input?.let { abs(it) }
            return Variable(Type.FLOAT, result)
        }
        else if(node is NodeFloatDiv) {
            var sum = 0.0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]
                input?.let { sum /= input }
            }
            return Variable(Type.FLOAT, sum)
        }
        else if(node is NodeFloatEqual) {
            val first = node.dependencies[NodeFloatEqual.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatEqual.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first == second)
        }
        else if(node is NodeFloatLess) {
            val first = node.dependencies[NodeFloatLess.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatLess.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first < second)
        }
        else if(node is NodeFloatLessOrEqual) {
            val first = node.dependencies[NodeFloatLessOrEqual.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatLessOrEqual.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first <= second)
        }
        else if(node is NodeFloatMax) {
            var max = Double.POSITIVE_INFINITY
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]!!
                max = max(max, input)
            }
            return Variable(Type.FLOAT, max)
        }
        else if(node is NodeFloatMin) {
            var min = Double.POSITIVE_INFINITY
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]!!
                min = min(min, input)
            }
            return Variable(Type.FLOAT, min)
        }
        else if(node is NodeFloatMod) {
            var sum = 0.0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]
                input?.let { sum %= input }
            }
            return Variable(Type.FLOAT, sum)
        }
        else if(node is NodeFloatMore) {
            val first = node.dependencies[NodeFloatMore.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatMore.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first > second)
        }
        else if(node is NodeFloatMoreOrEqual) {
            val first = node.dependencies[NodeFloatMoreOrEqual.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatMoreOrEqual.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first >= second)
        }
        else if(node is NodeFloatMul) {
            var mul = 0.0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]
                input?.let { mul *= input }
            }
            return Variable(Type.FLOAT, mul)
        }
        else if(node is NodeFloatNotEqual) {
            val first = node.dependencies[NodeFloatNotEqual.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatNotEqual.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.BOOLEAN, first != second)
        }
        else if(node is NodeFloatSub) {
            val first = node.dependencies[NodeFloatSub.FIRST]!!.invoke(context)[Type.FLOAT]!!
            val second = node.dependencies[NodeFloatSub.SECOND]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.FLOAT, first - second)
        }
        else if(node is NodeFloatSum) {
            var sum = 0.0
            for (i in node.dependencies.values.indices) {
                val input = node.dependencies[i]!!.invoke(context)[Type.FLOAT]
                input?.let { sum += input }
            }
            return Variable(Type.FLOAT, sum)
        }
        else if(node is NodeLog) {
            val number = node.dependencies[NodeLog.NUMBER]!!.invoke(context)[Type.FLOAT]!!
            val base = node.dependencies[NodeLog.BASE]!!.invoke(context)[Type.FLOAT]!!
            return Variable(Type.FLOAT, log(number, base))
        }
        throw createIllegalException(node)
    }

}