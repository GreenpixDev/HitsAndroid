package ru.hits.android.axolot.executor.node.service

import ru.hits.android.axolot.executor.node.Node

interface NodeService<T> {

    fun invoke(node: Node): T

}