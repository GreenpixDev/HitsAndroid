package ru.hits.android.axolot.blueprint.node.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeGetVariable(val name: String) : NodeFunction() {

    override operator fun invoke(context: InterpreterContext): Variable {
        return context.scope.getVariable(name)
    }
}