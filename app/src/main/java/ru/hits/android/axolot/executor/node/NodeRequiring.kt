package ru.hits.android.axolot.executor.node

interface NodeRequiring : Node {

    val dependencies: List<NodeDependency>

}