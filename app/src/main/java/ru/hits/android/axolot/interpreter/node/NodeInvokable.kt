package ru.hits.android.axolot.interpreter.node

import ru.hits.android.axolot.interpreter.InterpreterContext

interface NodeInvokable<T> : Node {

    operator fun invoke(context: InterpreterContext): T

}