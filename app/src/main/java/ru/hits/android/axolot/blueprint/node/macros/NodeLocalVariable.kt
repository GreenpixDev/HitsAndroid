package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeLocalVariable(private val index: Int, val type: VariableType<*>) : NodeFunction() {

    override fun invoke(context: InterpreterContext): Variable {
        if (context.stack.peek().variables.size < index) {
            context.stack.peek().add(Variable.nullVariable(type))
        }

        return context.stack.peek()[index]
    }
}