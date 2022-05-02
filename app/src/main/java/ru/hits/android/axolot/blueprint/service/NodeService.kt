package ru.hits.android.axolot.blueprint.service

import ru.hits.android.axolot.blueprint.node.Node

interface NodeService<T> {

    fun invoke(node: Node): T

}