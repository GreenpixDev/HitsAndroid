package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.interpreter.type.VariableType

interface DeclaredDataPin : DeclaredPin {

    val type: VariableType<*>

}