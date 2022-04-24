package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.Interpreter

interface Context {

    val interpreter: Interpreter

    @Deprecated("Используйте NodeDependency#invoke(Context) для получения зависимого параметра")
    val params: Map<Any, Variable>

    val local: MutableMap<NodeExecutable, Array<Any>>

    val scope: Scope
        get() = interpreter.scope

    @Deprecated("Используйте Context#createChild()")
    fun createChild(params: Map<Any, Variable>): Context

    fun createChild(): Context

}