package ru.hits.android.axolot.blueprint.service

import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.reflect.KClass

class NodeHandlerService(private val nodeToService: Map<KClass<*>, NodeService<*>>) {

    fun invoke(node: NodeDependency): Variable {
        return nodeToService[node::class]?.invoke(node) as Variable
    }

    fun invoke(node: NodeExecutable): NodeExecutable {
        return nodeToService[node::class]?.invoke(node) as NodeExecutable
    }

}