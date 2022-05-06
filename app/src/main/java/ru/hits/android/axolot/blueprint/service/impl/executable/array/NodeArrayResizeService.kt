package ru.hits.android.axolot.blueprint.service.impl.executable.array

import ru.hits.android.axolot.blueprint.node.Node
import ru.hits.android.axolot.blueprint.node.NodeExecutable
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayResize
import ru.hits.android.axolot.blueprint.service.NodeExecutableService
import ru.hits.android.axolot.blueprint.service.NodeHandlerService
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.type.structure.ArrayType
import ru.hits.android.axolot.blueprint.variable.Variable
import ru.hits.android.axolot.interpreter.InterpreterContext

class NodeArrayResizeService(private val nodeHandlerService: NodeHandlerService) :
    NodeExecutableService {

    override fun invoke(node: Node, context: InterpreterContext): NodeExecutable? {
        if (node is NodeArrayResize) {
            val arrayReference =
                nodeHandlerService.invoke(node.dependencies[NodeArrayResize.ARRAY]!!, context)
            val elementType = (arrayReference.type as ArrayType).elementType
            val size = nodeHandlerService.invoke(
                node.dependencies[NodeArrayResize.SIZE]!!,
                context
            )[Type.INT]!!

            arrayReference.value = Array(size) { Variable.nullVariable(elementType) }

            return node.nextNode
        }
        throw createIllegalException(node)
    }

}