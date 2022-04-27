package ru.hits.android.axolot.executor.node.entity

import ru.hits.android.axolot.executor.node.NodeDependency
import ru.hits.android.axolot.executor.node.NodeExecutable

class NodePrintln : NodeExecutable() {

    var nextNode: NodeExecutable? = null

    fun initialize(param: NodeDependency) {
        dependencies.add(param)
    }

    override fun invoke(): NodeExecutable? {
        dependencies[0].invoke()

        // TODO Изменить на нашу консоль
        println()
        return nextNode
    }
}