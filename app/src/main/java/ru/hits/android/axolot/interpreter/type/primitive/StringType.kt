package ru.hits.android.axolot.interpreter.type.primitive

import ru.hits.android.axolot.interpreter.variable.Variable

class StringType : PrimitiveType<String> {

    override val defaultValue: String
        get() = ""

    override fun cast(variable: Variable): String {
        return variable.value.toString()
    }

    override fun toString(): String {
        return "String"
    }

}