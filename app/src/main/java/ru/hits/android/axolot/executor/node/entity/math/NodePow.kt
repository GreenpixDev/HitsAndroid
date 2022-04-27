package ru.hits.android.axolot.executor.node.entity.math


import ru.hits.android.axolot.executor.node.NodeDependency
import ru.hits.android.axolot.executor.node.NodeFunction

class NodePow : NodeFunction() {
    companion object {
        const val NUMBER = 0
        const val POW = 1
    }

    fun init(number: NodeDependency, pow: NodeDependency) {
        dependencies.add(number)
        dependencies.add(pow)
    }

}