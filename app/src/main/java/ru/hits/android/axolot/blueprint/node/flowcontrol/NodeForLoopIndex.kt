package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopIndex(private val index: Int) : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        return context.stack.peek()[index]
    }
}