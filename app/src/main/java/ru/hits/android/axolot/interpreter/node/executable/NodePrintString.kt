package ru.hits.android.axolot.interpreter.node.executable

import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.type.Type

class NodePrintString : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val STRING = 0
        var console: Console? = null
    }

    fun init(string: NodeDependency) {
        dependencies[STRING] = string
    }

    override operator fun invoke(context: InterpreterContext): NodeExecutable? {
        val string = dependencies[STRING]!!.invoke(context)[Type.STRING]

        return nextNode
    }
}