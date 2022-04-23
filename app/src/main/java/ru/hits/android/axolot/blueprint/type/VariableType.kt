package ru.hits.android.axolot.blueprint.type

import ru.hits.android.axolot.blueprint.variable.Variable

interface VariableType<T> : Type {

    fun cast(variable: Variable): T

}