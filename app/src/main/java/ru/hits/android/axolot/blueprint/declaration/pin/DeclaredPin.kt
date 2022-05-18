package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.interpreter.node.Node

/**
 * Интерфейс, описывающий декларацию пина у блока
 */
interface DeclaredPin {

    /**
     * Создать все пины для блока
     */
    fun createAllPin(owner: AxolotOwner): Collection<Pin>

    /**
     * Проинициализировать пин (на этапе компиляции)
     */
    fun handle(target: Collection<Node>, node: Node)

}