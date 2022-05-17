package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.TypedPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.type.VariableType

class DeclaredVarargInputDataPin @JvmOverloads constructor(
    private val minArgs: Int,
    private val handler: (Collection<Node>, NodeDependency) -> Unit,
    private val lazyName: (Int) -> String = { "$it" },
    override val lazyType: () -> VariableType<*>
) : DeclaredDataPin {

    constructor(
        minArgs: Int,
        handler: (Collection<Node>, NodeDependency) -> Unit,
        lazyName: (Int) -> String = { "$it" },
        type: VariableType<*>
    ) : this(minArgs, handler, lazyName, { type })

    override fun handle(target: Collection<Node>, node: Node) {
        handler.invoke(target, node as NodeDependency)
    }

    override fun createAllPin(owner: AxolotOwner): Collection<InputDataPin> {
        return Array(minArgs) {
            InputDataPin(
                owner,
                this,
                lazyName.invoke(it + 1)
            )
        }.toList()
    }

    fun createOnePin(owner: AxolotOwner): InputDataPin {
        var index = 1
        if (owner is AxolotBlock) {
            index += owner.contacts
                .filterIsInstance<TypedPin>()
                .filter { it.type == this }
                .count()
        }
        return InputDataPin(
            owner,
            this,
            lazyName.invoke(index)
        )
    }
}