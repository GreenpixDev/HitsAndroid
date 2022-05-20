package ru.hits.android.axolot.interpreter.service.impl.executable.regex

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexMatch
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexMatchService(private val handlerService: NodeHandlerService) :
    NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeRegexMatch) {
            val regexText =
                handlerService.invoke(node[NodeRegexMatch.REGEX_TEXT], context)[Type.STRING]!!
            val text = handlerService.invoke(node[NodeRegexMatch.TEXT], context)[Type.STRING]!!

            val matchesRegex = Regex(regexText).matches(text)

            return Variable(Type.BOOLEAN, matchesRegex)

        } else throw IllegalStateException("Что-то пошло не так")
    }
}