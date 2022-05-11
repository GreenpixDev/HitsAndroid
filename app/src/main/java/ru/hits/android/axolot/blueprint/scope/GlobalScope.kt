package ru.hits.android.axolot.blueprint.scope

import ru.hits.android.axolot.blueprint.variable.Variable

class GlobalScope : Scope {

    private val variables: MutableMap<String, Variable> = mutableMapOf()

    override fun declareVariable(name: String, variable: Variable) {
        variables[name] = variable
    }

    @Suppress("unchecked_cast")
    override fun setVariable(name: String, variable: Variable) {
        val scopeVariable = variables[name]
        requireNotNull(scopeVariable) { "variable $name not declared" }
        require(scopeVariable.type == variable.type) {
            "attempt to set the variable $name a value with illegal type, " +
                    "excepted ${scopeVariable.type}, actual ${variable.type}"
        }
        variables[name] = variable
    }

    override fun getVariable(name: String): Variable {
        return variables[name]!!
    }

}