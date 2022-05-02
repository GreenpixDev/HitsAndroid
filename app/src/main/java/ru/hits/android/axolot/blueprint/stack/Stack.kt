package ru.hits.android.axolot.blueprint.stack

import ru.hits.android.axolot.blueprint.variable.Variable

class Stack {

    private val stack: MutableList<MutableMap<Any, Variable>> = mutableListOf()

    init {
        push()
    }

    fun push(): MutableMap<Any, Variable> {
        val map = mutableMapOf<Any, Variable>()
        stack.add(map)
        return map
    }

    fun pop(): MutableMap<Any, Variable> {
        return stack.removeLast()
    }

    fun peek(): MutableMap<Any, Variable> {
        return stack.last()
    }
}