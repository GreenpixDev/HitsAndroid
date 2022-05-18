package ru.hits.android.axolot.interpreter.type

import ru.hits.android.axolot.interpreter.variable.Variable

interface VariableType<T> : Type {

    val defaultValue: T?

    fun cast(variable: Variable): T

}