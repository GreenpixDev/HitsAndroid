package ru.hits.android.axolot.blueprint.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeExecutable

class NodeMacrosInput : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        return nextNode
    }
}