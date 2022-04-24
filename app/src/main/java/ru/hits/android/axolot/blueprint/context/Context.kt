package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.interpreter.Interpreter

interface Context {

    val interpreter: Interpreter

    val local: MutableMap<NodeExecutable, Array<Any>>

    val scope: Scope
        get() = interpreter.scope

    fun createChild(): Context

}