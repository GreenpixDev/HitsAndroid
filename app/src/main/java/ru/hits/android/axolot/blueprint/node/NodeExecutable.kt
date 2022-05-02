package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.util.Dependencies

abstract class NodeExecutable : NodeRequiring, NodeInvokable<NodeExecutable?> {

    override val dependencies: Dependencies = Dependencies()

}