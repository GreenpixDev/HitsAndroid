package ru.hits.android.axolot.blueprint.service

import ru.hits.android.axolot.blueprint.node.NodeConstant
import ru.hits.android.axolot.blueprint.node.executable.NodePrintString
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntEqual
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntSum
import ru.hits.android.axolot.blueprint.service.impl.executable.NodePrintStringService
import ru.hits.android.axolot.blueprint.service.impl.flowcontrol.NodeBranchService
import ru.hits.android.axolot.blueprint.service.impl.function.NodeCastService
import ru.hits.android.axolot.blueprint.service.impl.function.NodeGetVariableService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionEndService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionInvokeService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionParameterService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionReturnedService
import ru.hits.android.axolot.blueprint.service.impl.function.math.integer.NodeIntEqualService
import ru.hits.android.axolot.blueprint.service.impl.function.math.integer.NodeIntSumService
import kotlin.reflect.KClass

class ServiceInit {

    fun intiHandler():Map<KClass<*>, NodeService<*>> {
        val map = hashMapOf<KClass<*>, NodeService<*>>()
        map[NodePrintString::class] = NodePrintStringService()
        map[NodeGetVariable::class] = NodeGetVariableService()
        map[NodeBranch::class] = NodeBranchService()
        map[NodeIntEqual::class] = NodeIntEqualService()
        map[NodeFunctionParameter::class] = NodeFunctionParameterService()
        map[NodeFunctionEnd::class] = NodeFunctionEndService()
        map[NodeFunctionInvoke::class] = NodeFunctionInvokeService()
        map[NodeFunctionReturned::class] = NodeFunctionReturnedService()
        map[NodeIntEqual::class] = NodeIntEqualService()
        map[NodeIntSum::class] = NodeIntSumService()
        map[NodeConstant::class] = NodeConstantService()
        map[NodeCast::class] = NodeCastService()
        return map
    }

}