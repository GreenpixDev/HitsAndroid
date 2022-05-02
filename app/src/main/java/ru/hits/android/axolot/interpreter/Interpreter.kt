package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.blueprint.node.NodeExecutable

interface Interpreter {

    fun execute(node: NodeExecutable?)

}