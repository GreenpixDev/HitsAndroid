package ru.hits.android.axolot.interpreter.stack

import ru.hits.android.axolot.interpreter.variable.Variable

class StackFrame(val invocable: Any) {

    val variables: MutableMap<Any, Variable> = mutableMapOf()

}