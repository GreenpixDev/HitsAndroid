package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputDataPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeDependency

class DeclaredSingleOutputDataPin @JvmOverloads constructor(
    private val nodeFabric: () -> NodeDependency,
    private val name: String = "",
) : DeclaredAutonomicPin {

    override fun handle(target: Collection<Node>, node: Node) {
        // Должен быть пустым
    }

    override fun createPin(owner: AxolotOwner): Collection<OutputDataPin> {
        return listOf(OutputDataPin(owner, this, name))
    }

    override fun createNode(owner: AxolotOwner): Node {
        return nodeFabric.invoke()
    }

}