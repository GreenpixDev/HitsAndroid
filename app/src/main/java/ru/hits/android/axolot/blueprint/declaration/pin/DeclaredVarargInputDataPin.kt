package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeDependency

class DeclaredVarargInputDataPin @JvmOverloads constructor(
    private val minArgs: Int,
    private val handler: (Collection<Node>, NodeDependency) -> Unit,
    private val namePattern: (Int) -> String = { "$it" }
) : DeclaredPin {

    @Suppress("unchecked_cast")
    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeDependency)
    }

    override fun createPin(owner: AxolotOwner): Collection<InputDataPin> {
        return Array(minArgs) { InputDataPin(owner, this, namePattern.invoke(it + 1)) }.toList()
    }

}