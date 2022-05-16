package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.type.VariableType

class DeclaredSingleInputDataPin @JvmOverloads constructor(
    private val handler: (Collection<Node>, NodeDependency) -> Unit,
    private val lazyName: () -> String = { "" },
    override val lazyType: () -> VariableType<*>
) : DeclaredDataPin {

    constructor(
        handler: (Collection<Node>, NodeDependency) -> Unit,
        name: String,
        type: VariableType<*>
    ) : this(handler, { name }, { type })

    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeDependency)
    }

    override fun createPin(owner: AxolotOwner): Collection<InputDataPin> {
        return listOf(InputDataPin(owner, this, lazyName.invoke()))
    }

}