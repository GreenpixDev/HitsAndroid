package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.Interpreter

class CacheableContext(override val interpreter: Interpreter,
                       override val params: Map<Any, Variable>,
                       override val local: MutableMap<NodeExecutable, Array<Any>>,
                       val cache: MutableMap<NodeFunction, Variable>) : Context {

    override fun createChild(params: Map<Any, Variable>): CacheableContext {
        return CacheableContext(interpreter, params, local, cache)
    }

}