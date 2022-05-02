package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.blueprint.type.VariableType

class BlueprintFunction(val inputExecutable: NodeExecutable) {

    val input: MutableMap<String, NodeFunctionParameter> = mutableMapOf()

    val output: MutableMap<String, VariableType<*>> = mutableMapOf()

}