package ru.hits.android.axolot.interpreter.node.macros

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class NodeMacrosOutput : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        return nextNode
    }
}