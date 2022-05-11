package ru.hits.android.axolot.interpreter.node

interface NodeRequiring : Node {

    val dependencies: Map<Any, NodeDependency>

}