package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.Interpreter

@Deprecated("Кэш устарел из-за изменений в архитектуре")
class CacheableContext(override val interpreter: Interpreter,
                       override val params: Map<Any, Variable>,
                       override val local: MutableMap<NodeExecutable, Array<Any>>,
                       val cache: MutableMap<NodeFunction, Variable>) : Context {

    override fun createChild(params: Map<Any, Variable>): CacheableContext {
        return CacheableContext(interpreter, params, local, cache)
    }

    override fun createChild(): Context {
        return CacheableContext(interpreter, emptyMap(), local, cache)
    }

}