package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.stack.Stack
import ru.hits.android.axolot.interpreter.Interpreter

interface Context {

    val interpreter: Interpreter

    @Deprecated("Используйте stack")
    val local: MutableMap<Node, LocalContext>

    val scope: Scope
        get() = interpreter.scope

    val stack: Stack
        get() = interpreter.stack.get()

    fun createChild(): Context

    @Deprecated("Используйте stack")
    fun createLocalContext(node: Node, map: Map<Any, Any?>): LocalContext

}