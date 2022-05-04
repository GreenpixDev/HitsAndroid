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
import ru.hits.android.axolot.blueprint.node.function.math.bool.*
import ru.hits.android.axolot.blueprint.node.function.math.integer.*
import ru.hits.android.axolot.blueprint.service.impl.executable.NodePrintStringService
import ru.hits.android.axolot.blueprint.service.impl.flowcontrol.NodeBranchService
import ru.hits.android.axolot.blueprint.service.impl.function.NodeCastService
import ru.hits.android.axolot.blueprint.service.impl.function.NodeGetVariableService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionEndService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionInvokeService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionParameterService
import ru.hits.android.axolot.blueprint.service.impl.function.custom.NodeFunctionReturnedService
import ru.hits.android.axolot.blueprint.service.impl.function.math.bool.NodeBooleanService
import ru.hits.android.axolot.blueprint.service.impl.function.math.integer.NodeIntService
import kotlin.reflect.KClass

class ServiceInit {

    fun intiHandler():Map<KClass<*>, NodeService<*>> {
        val map = hashMapOf<KClass<*>, NodeService<*>>()
        map[NodePrintString::class] = NodePrintStringService()
        map[NodeGetVariable::class] = NodeGetVariableService()
        map[NodeBranch::class] = NodeBranchService()
        map[NodeFunctionParameter::class] = NodeFunctionParameterService()
        map[NodeFunctionEnd::class] = NodeFunctionEndService()
        map[NodeFunctionInvoke::class] = NodeFunctionInvokeService()
        map[NodeFunctionReturned::class] = NodeFunctionReturnedService()
        map[NodeConstant::class] = NodeConstantService()
        map[NodeCast::class] = NodeCastService()
        //------------------------ Math for int
        map[NodeIntDiv::class] = NodeIntService()
        map[NodeIntEqual::class] = NodeIntService()
        map[NodeIntLess::class] = NodeIntService()
        map[NodeIntLessOrEqual::class] = NodeIntService()
        map[NodeIntMax::class] = NodeIntService()
        map[NodeIntMin::class] = NodeIntService()
        map[NodeIntMod::class] = NodeIntService()
        map[NodeIntMore::class] = NodeIntService()
        map[NodeIntMoreOrEqual::class] = NodeIntService()
        map[NodeIntMul::class] = NodeIntService()
        map[NodeIntNotEqual::class] = NodeIntService()
        map[NodeIntSub::class] = NodeIntService()
        map[NodeIntSum::class] = NodeIntService()
        //------------------------ Math for boolean
        map[NodeBooleanAnd::class] = NodeBooleanService()
        map[NodeBooleanNand::class] = NodeBooleanService()
        map[NodeBooleanNor::class] = NodeBooleanService()
        map[NodeBooleanNot::class] = NodeBooleanService()
        map[NodeBooleanOr::class] = NodeBooleanService()
        map[NodeBooleanXnor::class] = NodeBooleanService()
        map[NodeBooleanXor::class] = NodeBooleanService()
        return map
    }

}