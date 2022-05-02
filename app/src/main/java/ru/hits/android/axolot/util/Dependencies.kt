package ru.hits.android.axolot.util

import ru.hits.android.axolot.blueprint.node.NodeDependency

class Dependencies : ArrayList<NodeDependency>() {

    override fun set(index: Int, element: NodeDependency): NodeDependency {
        if (index >= size) {
            for (i in index..size) {
                super.add(NodeDependencyMock(i))
            }
        }
        return super.set(index, element)
    }
}