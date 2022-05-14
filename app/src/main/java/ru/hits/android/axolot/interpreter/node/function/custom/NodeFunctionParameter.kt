package ru.hits.android.axolot.interpreter.node.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFunctionParameter : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        return context.stack[this]!!
    }
}