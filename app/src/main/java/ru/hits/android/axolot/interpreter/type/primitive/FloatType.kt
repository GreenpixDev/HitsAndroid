package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class FloatType : NumberType<Double>() {

    override val defaultValue: Double
        get() = 0.0

    override fun cast(variable: Variable): Double {
        if (variable.type is NumberType<*>) {
            return (variable.value as Number).toDouble()
        }
        if (variable.type is StringType) {
            return variable.value.toString().toDouble()
        }
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "Float"
    }

}