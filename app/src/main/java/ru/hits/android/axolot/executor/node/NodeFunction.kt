package ru.hits.android.axolot.executor.node

abstract class NodeFunction : NodeRequiring, NodeDependency {

    override val dependencies: MutableList<NodeDependency> = mutableListOf()

}