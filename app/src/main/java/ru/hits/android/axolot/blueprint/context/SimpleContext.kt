package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.interpreter.Interpreter

class SimpleContext(override val interpreter: Interpreter,
                    override val local: MutableMap<NodeExecutable, Array<Any>>) : Context {

    override fun createChild(): Context {
        return SimpleContext(interpreter, local)
    }

}