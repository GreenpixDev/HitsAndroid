package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class IntType : NumberType<Int>() {

    override val defaultValue: Int
        get() = 0

    override fun cast(variable: Variable): Int {
        if (variable.type is NumberType<*>) {
            return (variable.value as Number).toInt()
        }
        if (variable.type is StringType) {
            return variable.value.toString().toInt()
        }
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "Int"
    }

}