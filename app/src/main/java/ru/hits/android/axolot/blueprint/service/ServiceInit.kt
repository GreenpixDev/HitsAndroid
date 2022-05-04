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
        val nodeIntService = NodeIntService()
        map[NodeIntDiv::class] = nodeIntService
        map[NodeIntEqual::class] = nodeIntService
        map[NodeIntLess::class] = nodeIntService
        map[NodeIntLessOrEqual::class] = nodeIntService
        map[NodeIntMax::class] = nodeIntService
        map[NodeIntMin::class] = nodeIntService
        map[NodeIntMod::class] = nodeIntService
        map[NodeIntMore::class] = nodeIntService
        map[NodeIntMoreOrEqual::class] = nodeIntService
        map[NodeIntMul::class] = nodeIntService
        map[NodeIntNotEqual::class] = nodeIntService
        map[NodeIntSub::class] = nodeIntService
        map[NodeIntSum::class] = nodeIntService
        //------------------------ Math for boolean
        val nodeBooleanService = NodeBooleanService()
        map[NodeBooleanAnd::class] = nodeBooleanService
        map[NodeBooleanNand::class] = nodeBooleanService
        map[NodeBooleanNor::class] = nodeBooleanService
        map[NodeBooleanNot::class] = nodeBooleanService
        map[NodeBooleanOr::class] = nodeBooleanService
        map[NodeBooleanXnor::class] = nodeBooleanService
        map[NodeBooleanXor::class] = nodeBooleanService
        return map
    }

}