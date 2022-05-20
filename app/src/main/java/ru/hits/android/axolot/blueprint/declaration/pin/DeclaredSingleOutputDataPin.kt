package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputDataPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.type.VariableType

class DeclaredSingleOutputDataPin constructor(
    private val nodeFabric: () -> NodeDependency,
    private val lazyName: () -> String,
    override val lazyType: () -> VariableType<*>
) : DeclaredAutonomicPin, DeclaredDataPin {

    override val name: String
        get() = lazyName.invoke()

    constructor(
        nodeFabric: () -> NodeDependency,
        name: String,
        type: VariableType<*>
    ) : this(nodeFabric, { name }, { type })

    constructor(
        nodeFabric: () -> NodeDependency,
        type: VariableType<*>
    ) : this(nodeFabric, { "" }, { type })

    override fun handle(target: Collection<Node>, node: Node) {
        // Должен быть пустым
    }

    override fun createAllPin(owner: AxolotOwner): Collection<OutputDataPin> {
        return listOf(OutputDataPin(owner, this, lazyName.invoke()))
    }

    override fun createNode(owner: AxolotOwner): Node {
        return nodeFabric.invoke()
    }

}