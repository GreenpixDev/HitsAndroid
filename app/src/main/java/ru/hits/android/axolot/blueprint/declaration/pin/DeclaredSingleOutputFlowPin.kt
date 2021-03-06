package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputFlowPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class DeclaredSingleOutputFlowPin constructor(
    private val handler: (Collection<Node>, NodeExecutable) -> Unit,
    private val lazyName: () -> String
) : DeclaredPin {

    constructor(
        handler: (Collection<Node>, NodeExecutable) -> Unit,
        name: String
    ) : this(handler, { name })

    constructor(
        handler: (Collection<Node>, NodeExecutable) -> Unit,
    ) : this(handler, { "" })

    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeExecutable)
    }

    override fun createAllPin(owner: AxolotOwner): Collection<OutputFlowPin> {
        return listOf(OutputFlowPin(owner, this, lazyName.invoke()))
    }

}