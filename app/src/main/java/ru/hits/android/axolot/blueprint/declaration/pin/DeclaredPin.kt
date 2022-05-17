package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.interpreter.node.Node
import java.io.Serializable

/**
 * Интерфейс, описывающий декларацию пина у блока
 */
interface DeclaredPin: Serializable {

    /**
     * Создать пин для блока
     */
    fun createPin(owner: AxolotOwner): Collection<Pin>

    /**
     * Проинициализировать пин (на этапе компиляции)
     */
    fun handle(target: Collection<Node>, node: Node)

}