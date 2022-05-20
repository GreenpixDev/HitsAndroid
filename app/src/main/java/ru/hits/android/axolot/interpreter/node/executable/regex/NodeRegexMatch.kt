package ru.hits.android.axolot.interpreter.node.executable.regex

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexMatch : NodeFunction() {

    companion object {
        const val TEXT = "text"
        const val REGEX_TEXT = ""
    }

    override fun invoke(context: InterpreterContext): Variable {
        throw UnsupportedOperationException("Не поддерживается")
    }


}