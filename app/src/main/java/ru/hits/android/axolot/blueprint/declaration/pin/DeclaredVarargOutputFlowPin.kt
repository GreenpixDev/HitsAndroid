package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputFlowPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class DeclaredVarargOutputFlowPin @JvmOverloads constructor(
    private val minArgs: Int,
    private val handler: (Collection<Node>, NodeExecutable) -> Unit,
    private val namePattern: (Int) -> String = { "$it" },
) : DeclaredPin {

    @Suppress("unchecked_cast")
    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeExecutable)
    }

    override fun createPin(owner: AxolotOwner): Collection<OutputFlowPin> {
        return Array(minArgs) { OutputFlowPin(owner, this, namePattern.invoke(it + 1)) }.toList()
    }

}