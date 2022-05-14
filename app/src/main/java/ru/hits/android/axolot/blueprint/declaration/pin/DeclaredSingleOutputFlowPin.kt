package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputFlowPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class DeclaredSingleOutputFlowPin @JvmOverloads constructor(
    private val handler: (Collection<Node>, NodeExecutable) -> Unit,
    private val name: String = ""
) : DeclaredPin {

    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeExecutable)
    }

    override fun createPin(owner: AxolotOwner): Collection<OutputFlowPin> {
        return listOf(OutputFlowPin(owner, this, name))
    }

}