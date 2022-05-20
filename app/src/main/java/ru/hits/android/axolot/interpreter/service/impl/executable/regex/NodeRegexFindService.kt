package ru.hits.android.axolot.interpreter.service.impl.executable.regex

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexFind
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeRegexFindService(private val handlerService: NodeHandlerService) : NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {

        if (node is NodeRegexFind) {
            val regexText =
                handlerService.invoke(node[NodeRegexFind.REGEX_TEXT], context)[Type.STRING]!!
            val text = handlerService.invoke(node[NodeRegexFind.TEXT], context)[Type.STRING]!!
            val startIndex =
                handlerService.invoke(node[NodeRegexFind.START_INDEX], context)[Type.INT]!!

            val findRegexText = Regex(regexText).find(text, startIndex)

            return Variable(Type.STRING, findRegexText)

        } else throw IllegalStateException("Что-то пошло не так")
    }
}