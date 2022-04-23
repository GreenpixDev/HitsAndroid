package ru.hits.android.axolot.blueprint.type.primitive

import ru.hits.android.axolot.blueprint.variable.Variable

class BooleanType : PrimitiveType<Boolean> {

    override fun cast(variable: Variable): Boolean {
        throw TypeCastException("cannot cast type ${variable.type} to ${toString()}")
    }

    override fun toString(): String {
        return "BOOLEAN"
    }

}