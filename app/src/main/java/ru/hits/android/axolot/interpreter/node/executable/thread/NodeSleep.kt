package ru.hits.android.axolot.interpreter.node.executable.thread

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.type.Type

class NodeSleep : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val MILLIS = 0
    }

    fun init(millis: NodeDependency) {
        dependencies[MILLIS] = millis
    }

    override operator fun invoke(context: InterpreterContext): NodeExecutable? {
        val millis = dependencies[MILLIS]!!.invoke(context)[Type.INT]!!
        Thread.sleep(millis.toLong())
        return nextNode
    }
}