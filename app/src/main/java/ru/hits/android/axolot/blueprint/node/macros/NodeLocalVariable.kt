package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeLocalVariable(val type: VariableType<*>) : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        if (this !in context.stack) {
            context.stack[this] = Variable.nullVariable(type)
        }

        return context.stack[this]!!
    }
}