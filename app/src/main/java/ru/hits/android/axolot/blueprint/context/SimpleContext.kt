package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.Interpreter

class SimpleContext(override val interpreter: Interpreter,
                    override val params: Map<Any, Variable>,
                    override val local: MutableMap<NodeExecutable, Array<Any>>) : Context {

    override fun createChild(params: Map<Any, Variable>): SimpleContext {
        return SimpleContext(interpreter, params, local)
    }

    override fun createChild(): Context {
        return SimpleContext(interpreter, emptyMap(), local)
    }

}