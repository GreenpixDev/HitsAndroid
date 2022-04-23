package ru.hits.android.axolot.blueprint.scope

import ru.hits.android.axolot.blueprint.type.VariableType
import ru.hits.android.axolot.blueprint.variable.Variable

interface Scope {

    fun declareVariable(name: String, type: VariableType<*>)

    fun setVariable(name: String, variable: Variable)

    fun getVariable(name: String): Variable

    fun <T : Any> declareVariable(name: String, type: VariableType<T>, value: T) {
        declareVariable(name, type)
        return setVariable(name, Variable(type, value))
    }
}