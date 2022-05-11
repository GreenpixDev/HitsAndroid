package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.interpreter.scope.Scope
import ru.hits.android.axolot.interpreter.stack.Stack

interface InterpreterContext {

    val interpreter: Interpreter

    val scope: Scope

    val stack: Stack

}