package ru.hits.android.axolot.interpreter.node.executable.regex

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexFind : NodeFunction() {

    companion object {
        const val TEXT = "text"
        const val REGEX_TEXT = ""
        const val START_INDEX = 0
    }

    override fun invoke(context: InterpreterContext): Variable {
        throw UnsupportedOperationException("Не поддерживается")
    }
}