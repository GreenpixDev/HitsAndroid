package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.interpreter.Interpreter

class SimpleContext(override val interpreter: Interpreter,
                    override val local: MutableMap<Node, LocalContext>) : Context {

    override fun createChild(): Context {
        return SimpleContext(interpreter, local)
    }

    override fun createLocalContext(node: Node, map: Map<Any, Any?>): LocalContext {
        val localContext = LocalContext(this, node, HashMap(map))
        local[node] = localContext
        return localContext
    }
}