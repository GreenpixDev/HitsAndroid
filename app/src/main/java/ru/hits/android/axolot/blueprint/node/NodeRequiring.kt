package ru.hits.android.axolot.blueprint.node

interface NodeRequiring : Node {

    val dependencies: Map<Any, NodeDependency>

    operator fun get(key: Any): NodeDependency {
        val dependency = dependencies[key]
        requireNotNull(dependency) { "Зависимость $key не найдена у узла ${this::class}" }
        return dependency
    }
}