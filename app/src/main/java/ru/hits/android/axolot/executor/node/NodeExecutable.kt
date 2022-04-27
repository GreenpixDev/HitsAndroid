package ru.hits.android.axolot.executor.node

abstract class NodeExecutable : NodeRequiring, NodeInvocable<NodeExecutable?> {

    override val dependencies: MutableList<NodeDependency> = mutableListOf()

}