package ru.hits.android.axolot.interpreter.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeLocalVariable(val type: VariableType<*>) : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        if (this !in context.stack) {
            context.stack[this] = Variable.nullVariable(type)
        }

        return context.stack[this]!!
    }
}