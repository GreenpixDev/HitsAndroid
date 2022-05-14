package ru.hits.android.axolot.interpreter.service.impl.executable.array

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.executable.array.NodeArrayResize
import ru.hits.android.axolot.interpreter.service.NodeExecutableService
import ru.hits.android.axolot.interpreter.service.NodeHandlerService
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.structure.ArrayType
import ru.hits.android.axolot.interpreter.variable.Variable

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