package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeMacrosOutput : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: Context): NodeExecutable? {
        return nextNode
    }
}