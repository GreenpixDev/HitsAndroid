package ru.hits.android.axolot.blueprint.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type

class NodePrintString : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val STRING = 0
    }

    fun init(string: NodeDependency) {
        dependencies[STRING] = string
    }

    override operator fun invoke(context: InterpreterContext): NodeExecutable? {
        val string = dependencies[STRING]!!.invoke(context)[Type.STRING]
        println(string)
        return nextNode
    }
}