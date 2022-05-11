package ru.hits.android.axolot.interpreter

import ru.hits.android.axolot.interpreter.node.NodeExecutable

interface Interpreter {

    fun execute(node: NodeExecutable?)

}