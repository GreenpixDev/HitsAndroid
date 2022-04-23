package ru.hits.android.axolot.blueprint.node

interface NodeRequiring : Node {

    val dependencies: Map<Any, NodeDependency>

}