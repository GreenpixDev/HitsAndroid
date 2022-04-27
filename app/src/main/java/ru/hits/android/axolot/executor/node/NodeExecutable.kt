package ru.hits.android.axolot.executor.node

abstract class NodeExecutable : NodeRequiring {

    override val dependencies: MutableList<NodeDependency> = mutableListOf()

}