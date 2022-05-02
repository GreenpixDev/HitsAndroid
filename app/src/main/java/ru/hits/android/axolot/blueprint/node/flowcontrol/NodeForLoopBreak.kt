package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeExecutable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopBreak(private val index: Int) : NodeExecutable() {

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        context.stack.peek()[index].value = true
        return null
    }
}