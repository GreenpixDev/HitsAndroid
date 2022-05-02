package ru.hits.android.axolot.blueprint.node.function.custom

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeFunctionInvoke : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: Context): NodeExecutable? {



        return nextNode
    }
}