package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.util.Dependencies

abstract class NodeFunction : NodeDependency, NodeRequiring {

    override val dependencies: Dependencies = Dependencies()

}