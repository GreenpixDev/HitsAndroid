package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.interpreter.scope.Scope
import ru.hits.android.axolot.interpreter.stack.Stack

class BlueprintInterpreterContext(override val interpreter: BlueprintInterpreter) : InterpreterContext {

    override val scope: Scope
        get() = interpreter.scope

    override val stack: Stack
        get() = interpreter.stack.get()

}