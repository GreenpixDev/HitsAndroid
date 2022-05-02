package ru.hits.android.axolot.blueprint.node.flowcontrol

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.Interpreter

@Deprecated("Этот узел можно сделать не нативным")
class NodeForLoopIndex(private val forLoop: NodeForLoop) : NodeFunction() {

    override fun invoke(context: Context): Variable {
        val index = context.local[forLoop]!![NodeForLoop.INDEX]
        return Variable(Type.INT, index)
    }
}