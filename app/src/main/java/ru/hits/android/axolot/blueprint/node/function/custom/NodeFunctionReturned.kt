package ru.hits.android.axolot.blueprint.node.function.custom

import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeFunctionReturned(private val index: Int) : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        return context.stack.peek()[index]
    }
}