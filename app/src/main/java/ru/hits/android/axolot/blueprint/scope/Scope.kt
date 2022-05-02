package ru.hits.android.axolot.blueprint.scope

import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

interface Scope {

    fun declareVariable(name: String, variable: Variable)

    fun setVariable(name: String, variable: Variable)

    fun getVariable(name: String): Variable

    fun declareVariable(name: String, type: VariableType<*>) {
        declareVariable(name, Variable.nullVariable(type))
    }

    fun <T : Any> declareVariable(name: String, type: VariableType<T>, value: T) {
        declareVariable(name, type)
        setVariable(name, Variable(type, value))
    }
}