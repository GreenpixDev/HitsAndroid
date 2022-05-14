package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.InputFlowPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable

class DeclaredSingleInputFlowPin @JvmOverloads constructor(
    private val nodeFabric: () -> NodeExecutable,
    private val name: String = ""
) : DeclaredAutonomicPin {

    override fun handle(target: Collection<Node>, node: Node) {
        // Должен быть пустым
    }

    override fun createPin(owner: AxolotOwner): Collection<InputFlowPin> {
        return listOf(InputFlowPin(owner, this, name))
    }

    override fun createNode(owner: AxolotOwner): Node {
        return nodeFabric.invoke()
    }

}