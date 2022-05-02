package ru.hits.android.axolot.blueprint.stack

import ru.hits.android.axolot.blueprint.variable.Variable

class StackFrame {

    val variables: MutableList<Variable> = arrayListOf()

    operator fun get(index: Int): Variable {
        return variables[index]
    }

    fun add(variable: Variable): Int {
        variables.add(variable)
        return variables.size
    }

    fun removeAt(index: Int): Variable {
        return  variables.removeAt(index)
    }
}