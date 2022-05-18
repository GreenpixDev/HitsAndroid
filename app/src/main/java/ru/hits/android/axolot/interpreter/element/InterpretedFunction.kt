package ru.hits.android.axolot.interpreter.element

import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter

class InterpretedFunction {

    var inputExecutable: NodeExecutable? = null

    val input: MutableMap<String, NodeFunctionParameter> = mutableMapOf()

}