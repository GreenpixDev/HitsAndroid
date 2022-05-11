package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class FloatType : NumberType<Double>() {

    override fun cast(variable: Variable): Double {
        if (variable.type is NumberType<*>) {
            return (variable.value as Number).toDouble()
        }
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "Float"
    }

}