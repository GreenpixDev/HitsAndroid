package ru.hits.android.axolot.blueprint.node.executable

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable

class NodePrintString : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    companion object {
        const val STRING = 0
    }

    fun init(string: NodeDependency) {
        dependencies[STRING] = string
    }

    override operator fun invoke(context: Context): NodeExecutable? {
        val string = context.params[STRING]!![Type.STRING]
        println(string)
        return nextNode
    }
}