package ru.hits.android.axolot.interpreter.node.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeInput : NodeFunction() {
    override operator fun invoke(context: InterpreterContext): Variable {
        return context.scope.getVariable("name")//TODO(TODO(Не работает))
    }
}