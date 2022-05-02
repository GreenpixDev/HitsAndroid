package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.stack.Stack
import ru.hits.android.axolot.interpreter.Interpreter

class BlueprintInterpreterContext(override val interpreter: BlueprintInterpreter) : InterpreterContext {

    override val scope: Scope
        get() = interpreter.scope

    override val stack: Stack
        get() = interpreter.stack.get()

}