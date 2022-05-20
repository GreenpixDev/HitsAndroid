package ru.hits.android.axolot.interpreter.service.impl.function

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.node.function.NodeMath
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import ru.hits.android.axolot.math.MathInterpreter
import ru.hits.android.axolot.util.filterValuesNotNull

class NodeMathService(
    private val nodeHandlerService: NodeHandlerService,
    private val mathInterpreter: MathInterpreter
) : NodeDependencyService {

    val blockedSymbols = "(){}_+=-/|&^!*%<>"

    override fun invoke(node: Node, context: InterpreterContext): Variable {

        if (node is NodeMath) {
            val scope = context.scope
            val params = scope.variables
                .filterValues { it.type == Type.INT || it.type == Type.FLOAT }
                .filterKeys {
                    for (element in blockedSymbols) {
                        if (element in it) {
                            false
                        }
                    }
                    true
                }
                .mapValues { it.value.value as Double? }
                .filterValuesNotNull()
            val string = nodeHandlerService.invoke(
                node[NodePrintString.STRING],
                context
            )[Type.STRING]

            val answer = mathInterpreter.calcExpression(string.toString(), params)
            return Variable(Type.FLOAT, answer)
        }
        throw createIllegalException(node)
    }

}