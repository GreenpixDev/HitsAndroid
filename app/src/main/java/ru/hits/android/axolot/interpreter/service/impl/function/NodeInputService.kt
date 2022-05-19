package ru.hits.android.axolot.interpreter.service.impl.function

import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node


import ru.hits.android.axolot.interpreter.node.function.NodeInput
import ru.hits.android.axolot.interpreter.service.NodeDependencyService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeInputService(val console: Console) : NodeDependencyService {
    override fun invoke(node: Node, context: InterpreterContext): Variable {
        if (node is NodeInput) {
            var value = console.getInApp()
            while (value == null) {
                value = console?.getInApp()
                Thread.yield()
            }
            return Variable(Type.STRING, value)
        }
        throw createIllegalException(node)
    }
}