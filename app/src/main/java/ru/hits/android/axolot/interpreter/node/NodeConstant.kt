package ru.hits.android.axolot.interpreter.node

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeConstant(type: VariableType<*>) : NodeDependency {

    companion object {

        fun <T> of(type: VariableType<T>, value: T): NodeConstant {
            val node = NodeConstant(type)
            node.variable.value = value
            return node
        }

        fun of(variable: Variable): NodeConstant {
            val node = NodeConstant(variable.type)
            node.variable.value = variable.value
            return node
        }

    }

    var variable: Variable = Variable.nullVariable(type)

    override fun invoke(context: InterpreterContext): Variable {
        return variable
    }

}