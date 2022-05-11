package ru.hits.android.axolot.interpreter.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.math.log

class NodeLog : NodeFunction() {

    companion object {
        const val NUMBER = "number"
        const val BASE = "base"
    }

    fun init(number: NodeDependency, base: NodeDependency) {
        dependencies[NUMBER] = number
        dependencies[BASE] = base
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val number = dependencies[NUMBER]!!.invoke(context)[Type.FLOAT]!!
        val base = dependencies[BASE]!!.invoke(context)[Type.FLOAT]!!
        return Variable(Type.FLOAT, log(number, base))
    }
}