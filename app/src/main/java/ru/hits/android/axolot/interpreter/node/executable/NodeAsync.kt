package ru.hits.android.axolot.interpreter.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class NodeAsync : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        throw UnsupportedOperationException("Не поддерживается")
    }

}