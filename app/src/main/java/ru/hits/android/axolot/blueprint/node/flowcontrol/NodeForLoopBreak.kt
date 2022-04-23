package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.variable.Variable

class NodeForLoopBreak(private val forLoop: NodeForLoop) : NodeExecutable() {

    override fun invoke(context: Context): NodeExecutable? {
        context.local[forLoop]!![NodeForLoop.BREAK] = true
        return null
    }
}