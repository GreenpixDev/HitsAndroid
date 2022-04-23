package ru.hits.android.axolot.blueprint.node.function

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeGetVariable(val name: String) : NodeFunction() {

    override operator fun invoke(context: Context): Variable {
        return context.scope.getVariable(name)
    }
}