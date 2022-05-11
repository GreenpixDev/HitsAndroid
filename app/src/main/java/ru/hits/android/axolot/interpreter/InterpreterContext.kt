package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.stack.Stack

interface InterpreterContext {

    val interpreter: Interpreter

    val scope: Scope

    val stack: Stack

}