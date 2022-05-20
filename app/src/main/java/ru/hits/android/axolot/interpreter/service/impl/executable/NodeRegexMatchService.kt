package ru.hits.android.axolot.interpreter.service.impl.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.NodeRegexMatch
import ru.hits.android.axolot.interpreter.node.executable.NodeRegexMatch.Companion.TEXT
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexMatchService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeRegexMatch) {
            //логика использования regexMatch

            return Variable(Type.STRING, TEXT)

        } else throw IllegalStateException("Что-то пошло не так")
    }
}