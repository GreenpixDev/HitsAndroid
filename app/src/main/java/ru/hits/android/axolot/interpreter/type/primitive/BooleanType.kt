package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class BooleanType : PrimitiveType<Boolean> {

    override val defaultValue: Boolean
        get() = false

    override fun cast(variable: Variable): Boolean {
        if (variable.type is StringType) {
            return variable.value.toString().toBoolean()
        }
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "Boolean"
    }

}