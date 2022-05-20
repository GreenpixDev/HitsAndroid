package ru.hits.android.axolot.interpreter.service.impl.executable

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.NodeRegexFind
import ru.hits.android.axolot.interpreter.node.executable.NodeRegexFind.Companion.TEXT
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexFindService : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {

        if (node is NodeRegexFind) {
            //логика использования regexFind

            return Variable(Type.STRING, TEXT)

        } else throw IllegalStateException("Что-то пошло не так")
    }
}