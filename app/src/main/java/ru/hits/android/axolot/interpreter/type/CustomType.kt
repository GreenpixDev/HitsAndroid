package ru.hits.android.axolot.interpreter.type

import ru.hits.android.axolot.interpreter.variable.Variable

class CustomType : VariableType<Any> {

    override val defaultValue: Any
        get() = TODO("Not yet implemented")

    override fun cast(variable: Variable): Any {
        TODO("Not yet implemented")
    }

}