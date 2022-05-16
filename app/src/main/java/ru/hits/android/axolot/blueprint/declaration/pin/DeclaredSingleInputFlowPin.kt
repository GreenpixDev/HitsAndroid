package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.InputFlowPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class DeclaredSingleInputFlowPin constructor(
    private val nodeFabric: () -> NodeExecutable,
    private val lazyName: () -> String,
) : DeclaredAutonomicPin {

    constructor(
        nodeFabric: () -> NodeExecutable,
        name: String
    ) : this(nodeFabric, { name })

    constructor(
        nodeFabric: () -> NodeExecutable
    ) : this(nodeFabric, { "" })

    override fun handle(target: Collection<Node>, node: Node) {
        // Должен быть пустым
    }

    override fun createAllPin(owner: AxolotOwner): Collection<InputFlowPin> {
        return listOf(InputFlowPin(owner, this, lazyName.invoke()))
    }

    override fun createNode(owner: AxolotOwner): Node {
        return nodeFabric.invoke()
    }

}