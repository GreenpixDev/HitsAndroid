package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosInput
import ru.hits.android.axolot.blueprint.node.macros.NodeMacrosOutput

class BlueprintMacros : BlueprintBlock {

    val inputExecutable: MutableMap<String, NodeMacrosInput> = mutableMapOf()

    val input: MutableMap<String, NodeMacrosDependency> = mutableMapOf()

    val outputExecutable: MutableMap<String, NodeMacrosOutput> = mutableMapOf()

    val output: MutableMap<String, NodeMacrosDependency> = mutableMapOf()

}