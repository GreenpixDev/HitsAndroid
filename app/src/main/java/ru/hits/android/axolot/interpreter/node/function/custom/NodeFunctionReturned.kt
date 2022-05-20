package ru.hits.android.axolot.interpreter.node.function.custom

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeFunctionReturned(val name: String) : NodeFunction() {

    constructor(nodeInvoke: NodeFunctionInvoke, name: String) : this(name) {
        this.nodeInvoke = nodeInvoke
    }

    lateinit var nodeInvoke: NodeFunctionInvoke

    override fun invoke(context: InterpreterContext): Variable {
        return context.stack[nodeInvoke to name]!!
    }
}