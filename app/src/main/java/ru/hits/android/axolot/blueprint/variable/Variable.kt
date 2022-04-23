package ru.hits.android.axolot.blueprint.variable

import ru.hits.android.axolot.blueprint.type.VariableType

class Variable(val type: VariableType<*>, var value: Any?) {

    @Suppress("unchecked_cast")
    operator fun <T> get(type: VariableType<T>): T? {
        require(this.type == type) { "invalid type of variable: excepted $type, actual ${this.type}" }
        return value as T
    }

    companion object {

        fun nullVariable(type: VariableType<*>): Variable {
            return Variable(type, null)
        }

    }

}