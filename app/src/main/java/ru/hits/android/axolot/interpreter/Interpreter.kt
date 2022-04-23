package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.scope.Scope

interface Interpreter {

    val scope: Scope

    fun execute(node: NodeExecutable?, context: Context)

    fun createContext(cache: Boolean): Context

}