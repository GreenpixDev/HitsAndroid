package ru.hits.android.axolot.blueprint.stack

class Stack {

    private val stack: MutableList<StackFrame> = arrayListOf()

    init {
        push(StackFrame())
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
}