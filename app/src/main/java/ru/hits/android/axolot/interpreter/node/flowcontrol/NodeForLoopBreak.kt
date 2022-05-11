package ru.hits.android.axolot.interpreter.node.flowcontrol

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeExecutable

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopBreak : NodeExecutable() {

    override fun invoke(context: InterpreterContext): NodeExecutable? {
        context.stack[this]?.value = true
        return null
    }
}