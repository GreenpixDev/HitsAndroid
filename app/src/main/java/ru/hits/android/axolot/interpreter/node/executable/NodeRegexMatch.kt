package ru.hits.android.axolot.interpreter.node.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexMatch : NodeDependency {

    var regexMatchText: NodeExecutable? = null

    companion object {
        const val TEXT = "text"
        const val REGEX_TEXT = ""
    }

    fun init() {

    }

    override fun invoke(context: InterpreterContext): Variable {
        //логика регулярки
    }


}