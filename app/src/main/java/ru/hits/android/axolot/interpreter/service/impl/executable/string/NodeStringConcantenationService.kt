package ru.hits.android.axolot.interpreter.service.impl.executable.string

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.string.NodeStringConcatenation
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeStringConcantenationService(private val handlerService: NodeHandlerService) :
    NodeDependencyService {

    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeStringConcatenation) {
            var sum = ""

            for (i in node.dependencies.values.indices) {
                val input =
                    handlerService.invoke(node.dependencies[i]!!, context)[Type.FLOAT]
                input?.let { sum += input }
            }

            return Variable(Type.STRING, sum)
        }

        throw createIllegalException(node)
    }

}