package ru.hits.android.axolot.blueprint.node

abstract class NodeExecutable : NodeRequiring, NodeInvokable<NodeExecutable?> {

    override val dependencies: MutableMap<Any, NodeDependency> = mutableMapOf()

}