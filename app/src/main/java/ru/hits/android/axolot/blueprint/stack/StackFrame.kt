package ru.hits.android.axolot.blueprint.stack

import ru.hits.android.axolot.blueprint.variable.Variable

class StackFrame(val invocable: Any) {

    val variables: MutableMap<Any, Variable> = mutableMapOf()

}