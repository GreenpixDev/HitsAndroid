package ru.hits.android.axolot.interpreter.service

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.variable.Variable
import kotlin.reflect.KClass

class NodeHandlerService() {

    private val nodeToService: MutableMap<KClass<*>, NodeService<*>> = hashMapOf()

    fun init(nodeToService : Map<KClass<*>, NodeService<*>>) {
        this.nodeToService.putAll(nodeToService)
    }

    fun invoke(node: NodeDependency, context: InterpreterContext): Variable {
        val service = nodeToService[node::class]
        requireNotNull(service) { "Сервис для узла ${node::class} не зарегистрирован" }
        return service.invoke(node, context) as Variable
    }

    fun invoke(node: NodeExecutable, context: InterpreterContext): NodeExecutable? {
        val service = nodeToService[node::class]
        requireNotNull(service) { "Сервис для узла ${node::class} не зарегистрирован" }
        return service.invoke(node, context) as? NodeExecutable
    }

}