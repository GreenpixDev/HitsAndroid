package ru.hits.android.axolot.interpreter.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexFind : NodeDependency {

    var regexFindText: NodeExecutable? = null

    companion object {
        const val TEXT = "text"
        const val TRUE = true
        const val FALSE = false
    }

    override fun invoke(context: InterpreterContext): Variable {
        throw UnsupportedOperationException("Не поддерживается")
    }
}