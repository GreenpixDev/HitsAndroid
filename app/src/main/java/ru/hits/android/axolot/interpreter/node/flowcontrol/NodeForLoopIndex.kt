package ru.hits.android.axolot.interpreter.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopIndex : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        return context.stack[this]!!
    }
}