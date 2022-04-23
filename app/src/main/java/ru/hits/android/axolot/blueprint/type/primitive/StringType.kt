package ru.hits.android.axolot.blueprint.type.primitive

import ru.hits.android.axolot.blueprint.variable.Variable

class StringType : PrimitiveType<String> {

    override fun cast(variable: Variable): String {
        return variable.value.toString()
    }

    override fun toString(): String {
        return "STRING"
    }

}