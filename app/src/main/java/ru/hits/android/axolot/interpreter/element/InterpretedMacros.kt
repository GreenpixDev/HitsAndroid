package ru.hits.android.axolot.interpreter.element

import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosOutput

class InterpretedMacros {

    val inputExecutable: MutableMap<String, NodeMacrosInput> = mutableMapOf()

    val input: MutableMap<String, NodeMacrosDependency> = mutableMapOf()

    val outputExecutable: MutableMap<String, NodeMacrosOutput> = mutableMapOf()

    val output: MutableMap<String, NodeMacrosDependency> = mutableMapOf()

}