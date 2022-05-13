package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class IntType : NumberType<Int>() {

    override fun cast(variable: Variable): Int {
        if (variable.type is NumberType<*>) {
            return (variable.value as Number).toInt()
        }
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "Int"
    }

}