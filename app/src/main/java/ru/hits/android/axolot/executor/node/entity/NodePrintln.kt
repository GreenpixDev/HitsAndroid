package ru.hits.android.axolot.executor.node.entity

import ru.hits.android.axolot.executor.node.NodeDependency
import ru.hits.android.axolot.executor.node.NodeExecutable

class NodePrintln : NodeExecutable() {

    companion object {
        const val PARAM = 0
    }

    var nextNode: NodeExecutable? = null

    fun init(param: NodeDependency) {
        dependencies.add(param)
    }

}