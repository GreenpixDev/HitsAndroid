package ru.hits.android.axolot.interpreter.node

abstract class NodeFunction : NodeDependency, NodeRequiring {

    override val dependencies: MutableMap<Any, NodeDependency> = mutableMapOf()

}