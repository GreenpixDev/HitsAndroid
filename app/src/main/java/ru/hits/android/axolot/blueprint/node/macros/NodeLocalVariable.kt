package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeLocalVariable(val type: VariableType<*>) : NodeFunction() {

    override fun invoke(context: Context): Variable {
        if (this !in context.stack.peek()) {
            context.stack.peek()[this] = Variable.nullVariable(type)
        }

        return context.stack.peek()[this]!!
    }
}