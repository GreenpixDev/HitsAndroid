package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.blueprint.variable.Variable

abstract class NodeFunction : NodeDependency, NodeRequiring, NodeInvokable<Variable> {

    override val dependencies: MutableMap<Any, NodeDependency> = mutableMapOf()

}