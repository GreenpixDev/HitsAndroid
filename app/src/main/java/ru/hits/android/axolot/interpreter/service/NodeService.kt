package ru.hits.android.axolot.interpreter.service

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node

interface NodeService<T> {


    fun invoke(node: Node, context: InterpreterContext): T

    fun createIllegalException(node: Node) : IllegalArgumentException{
        return IllegalArgumentException("Неверный тип ноды ${node::class}. Грусть, печаль, тоска :(")
    }

}
