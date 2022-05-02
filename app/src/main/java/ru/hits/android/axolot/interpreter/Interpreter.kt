package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.context.Context
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.scope.Scope
import ru.hits.android.axolot.blueprint.stack.Stack
import ru.hits.android.axolot.util.SuppliedThreadLocal

interface Interpreter {

    val scope: Scope

    val stack: SuppliedThreadLocal<Stack>

    fun execute(node: NodeExecutable?, context: Context)

    fun createContext(): Context

}