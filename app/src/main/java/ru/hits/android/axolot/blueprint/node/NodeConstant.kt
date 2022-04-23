package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeConstant(type: VariableType<*>) : NodeDependency {

    companion object {

        fun <T> of(type: VariableType<T>, value: T): NodeConstant {
            val node = NodeConstant(type)
            node.variable.value = value
            return node
        }

    }

    var variable: Variable = Variable.nullVariable(type)

}