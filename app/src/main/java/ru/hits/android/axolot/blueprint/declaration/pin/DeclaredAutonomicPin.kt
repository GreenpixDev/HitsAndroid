package ru.hits.android.axolot.blueprint.declaration.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import ru.hits.android.axolot.interpreter.node.Node

/**
 * Интерфейс автономного пина.
 * Автономный пин - такой пин, который является узлом на этапе интерпретации.
 * Обычно, это входные flow пины и выходные data пины, т.к. они требуют
 * в зависимости другие пины (точнее узлы на этапе интерпретации)
 */
interface DeclaredAutonomicPin : DeclaredPin {

    fun createNode(owner: AxolotOwner): Node

}