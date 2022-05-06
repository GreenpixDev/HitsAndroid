package ru.hits.android.axolot.blueprint.service

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.interpreter.InterpreterContext

interface NodeService<T> {


    fun invoke(node: Node, context: InterpreterContext): T

    fun createIllegalException(node: Node) : IllegalArgumentException{
        return IllegalArgumentException("Неверный тип ноды ${node::class}")
    }

}
