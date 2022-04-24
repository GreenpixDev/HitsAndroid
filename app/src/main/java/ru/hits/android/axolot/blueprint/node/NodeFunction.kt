package ru.hits.android.axolot.blueprint.node

abstract class NodeFunction : NodeDependency, NodeRequiring {

    override val dependencies: MutableMap<Any, NodeDependency> = mutableMapOf()

}