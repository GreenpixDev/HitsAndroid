package ru.hits.android.axolot.interpreter.element

import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.type.VariableType

class InterpretedFunction(val inputExecutable: NodeExecutable) {

    val input: MutableMap<String, NodeFunctionParameter> = mutableMapOf()

    val output: MutableMap<String, VariableType<*>> = mutableMapOf()

}