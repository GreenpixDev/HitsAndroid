package ru.hits.android.axolot.blueprint.stack

import ru.hits.android.axolot.blueprint.variable.Variable

class Stack {

    private val stack: MutableList<StackFrame> = mutableListOf()

    init {
        push(StackFrame(Thread.currentThread()))
    }

    fun push(frame: StackFrame) {
        stack.add(frame)
    }

    fun pop(): StackFrame {
        return stack.removeLast()
    }

    fun peek(): StackFrame {
        return stack.last()
    }

    fun peek(up: Int): StackFrame {
        return stack[stack.lastIndex - up]
    }

    operator fun get(key: Any): Variable? {
        return stack.last().variables[key]
    }

    operator fun set(key: Any, variable: Variable?) {
        if (variable != null) {
            stack.last().variables[key] = variable
        }
        else {
            stack.last().variables.remove(key)
        }
    }

    operator fun contains(key: Any): Boolean {
        return key in stack.last().variables
    }
}