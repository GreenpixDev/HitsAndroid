package ru.hits.android.axolot.interpreter.node.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeMath : NodeFunction() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val STRING = 0
    }

    fun init(string: NodeDependency) {
        dependencies[STRING] = string
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val string = dependencies[STRING]!!.invoke(context)[Type.STRING]
        throw UnsupportedOperationException("Не поддерживается")
    }

}