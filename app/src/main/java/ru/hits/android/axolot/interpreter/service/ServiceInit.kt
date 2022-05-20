package ru.hits.android.axolot.interpreter.service

import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.node.NodeConstant
import ru.hits.android.axolot.interpreter.node.executable.NodeAsync
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.node.executable.NodeSetVariable
import ru.hits.android.axolot.interpreter.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.interpreter.node.executable.array.NodeArrayResize
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexFind
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexMatch
import ru.hits.android.axolot.interpreter.node.executable.string.NodeStringConcatenation
import ru.hits.android.axolot.interpreter.node.executable.thread.NodeSleep
import ru.hits.android.axolot.interpreter.node.flowcontrol.*
import ru.hits.android.axolot.interpreter.node.function.NodeCast
import ru.hits.android.axolot.interpreter.node.function.NodeGetVariable
import ru.hits.android.axolot.interpreter.node.function.NodeInput
import ru.hits.android.axolot.interpreter.node.function.NodeMath
import ru.hits.android.axolot.interpreter.node.function.array.NodeArrayFindElement
import ru.hits.android.axolot.interpreter.node.function.array.NodeArrayGetElement
import ru.hits.android.axolot.interpreter.node.function.array.NodeArraySize
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.node.function.math.bool.*
import ru.hits.android.axolot.interpreter.node.function.math.integer.*
import ru.hits.android.axolot.interpreter.node.function.math.real.*
import ru.hits.android.axolot.interpreter.node.function.math.trig.*
import ru.hits.android.axolot.interpreter.node.macros.*
import ru.hits.android.axolot.interpreter.service.impl.executable.NodeAsyncService
import ru.hits.android.axolot.interpreter.service.impl.executable.NodePrintStringService
import ru.hits.android.axolot.interpreter.service.impl.executable.NodeSetVariableService
import ru.hits.android.axolot.interpreter.service.impl.executable.array.NodeArrayAssignElementService
import ru.hits.android.axolot.interpreter.service.impl.executable.array.NodeArrayResizeService
import ru.hits.android.axolot.interpreter.service.impl.executable.regex.NodeRegexFindService
import ru.hits.android.axolot.interpreter.service.impl.executable.regex.NodeRegexMatchService
import ru.hits.android.axolot.interpreter.service.impl.executable.string.NodeStringConcantenationService
import ru.hits.android.axolot.interpreter.service.impl.executable.thread.NodeSleepService
import ru.hits.android.axolot.interpreter.service.impl.flowcontrol.*
import ru.hits.android.axolot.interpreter.service.impl.function.NodeCastService
import ru.hits.android.axolot.interpreter.service.impl.function.NodeGetVariableService
import ru.hits.android.axolot.interpreter.service.impl.function.NodeInputService
import ru.hits.android.axolot.interpreter.service.impl.function.NodeMathService
import ru.hits.android.axolot.interpreter.service.impl.function.array.NodeArrayService
import ru.hits.android.axolot.interpreter.service.impl.function.custom.NodeFunctionEndService
import ru.hits.android.axolot.interpreter.service.impl.function.custom.NodeFunctionInvokeService
import ru.hits.android.axolot.interpreter.service.impl.function.custom.NodeFunctionParameterService
import ru.hits.android.axolot.interpreter.service.impl.function.custom.NodeFunctionReturnedService
import ru.hits.android.axolot.interpreter.service.impl.function.math.NodeBooleanService
import ru.hits.android.axolot.interpreter.service.impl.function.math.NodeFloatService
import ru.hits.android.axolot.interpreter.service.impl.function.math.NodeIntService
import ru.hits.android.axolot.interpreter.service.impl.function.math.NodeTrigService
import ru.hits.android.axolot.interpreter.service.impl.macros.*
import ru.hits.android.axolot.math.MathInterpreterImpl
import kotlin.reflect.KClass

class ServiceInit(private val nodeHandlerService: NodeHandlerService, val console: Console) {

    fun intiHandler(): Map<KClass<*>, NodeService<*>> {
        val map = hashMapOf<KClass<*>, NodeService<*>>()

        //------------------------ Regex
        map[NodeRegexMatch::class] = NodeRegexMatchService(nodeHandlerService)
        map[NodeRegexFind::class] = NodeRegexFindService(nodeHandlerService)

        //------------------------ NodeAsync
        map[NodeAsync::class] = NodeAsyncService()

        //------------------------ Macros
        map[NodeAssignVariable::class] = NodeAssignVariableService(nodeHandlerService)
        map[NodeLocalVariable::class] = NodeLocalVariableService()
        map[NodeMacrosDependency::class] = NodeMacrosDependencyService(nodeHandlerService)
        map[NodeMacrosInput::class] = NodeMacrosInputService()
        map[NodeMacrosOutput::class] = NodeMacrosOutputService()


        //------------------------ Flow control
        map[NodeBranch::class] = NodeBranchService(nodeHandlerService)
        map[NodeForLoopBreak::class] = NodeForLoopBreakService()
        map[NodeForLoopIndex::class] = NodeForLoopIndexService()
        map[NodeForLoop::class] = NodeForLoopService(nodeHandlerService)
        map[NodeSequence::class] = NodeSequenceService()
        map[NodeWhileLoop::class] = NodeWhileLoopService(nodeHandlerService)

        map[NodeFunctionParameter::class] = NodeFunctionParameterService()
        map[NodeFunctionEnd::class] = NodeFunctionEndService(nodeHandlerService)
        map[NodeFunctionInvoke::class] = NodeFunctionInvokeService(nodeHandlerService)
        map[NodeFunctionReturned::class] = NodeFunctionReturnedService()
        map[NodeConstant::class] = NodeConstantService()

        //------------------------ Math for string
        map[NodeStringConcatenation::class] = NodeStringConcantenationService(nodeHandlerService)

        //------------------------ Math for boolean
        val nodeBooleanService = NodeBooleanService(nodeHandlerService)
        map[NodeBooleanAnd::class] = nodeBooleanService
        map[NodeBooleanNand::class] = nodeBooleanService
        map[NodeBooleanNor::class] = nodeBooleanService
        map[NodeBooleanNot::class] = nodeBooleanService
        map[NodeBooleanOr::class] = nodeBooleanService
        map[NodeBooleanXnor::class] = nodeBooleanService
        map[NodeBooleanXor::class] = nodeBooleanService

        //------------------------ Math for int
        val nodeIntService = NodeIntService(nodeHandlerService)
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

        //------------------------ Math for float
        val nodeFloatService = NodeFloatService(nodeHandlerService)
        map[NodeFloatAbs::class] = nodeFloatService
        map[NodeFloatDiv::class] = nodeFloatService
        map[NodeFloatEqual::class] = nodeFloatService
        map[NodeFloatLess::class] = nodeFloatService
        map[NodeFloatLessOrEqual::class] = nodeFloatService
        map[NodeFloatMax::class] = nodeFloatService
        map[NodeFloatMin::class] = nodeFloatService
        map[NodeFloatMod::class] = nodeFloatService
        map[NodeFloatMore::class] = nodeFloatService
        map[NodeFloatMoreOrEqual::class] = nodeFloatService
        map[NodeFloatMul::class] = nodeFloatService
        map[NodeFloatNotEqual::class] = nodeFloatService
        map[NodeFloatSub::class] = nodeFloatService
        map[NodeFloatSum::class] = nodeFloatService
        map[NodeLog::class] = nodeFloatService

        //------------------------ trig math
        val nodeTrigService = NodeTrigService(nodeHandlerService)
        map[NodeArcCos::class] = nodeTrigService
        map[NodeArcSin::class] = nodeTrigService
        map[NodeArcTan::class] = nodeTrigService
        map[NodeCos::class] = nodeTrigService
        map[NodeSin::class] = nodeTrigService
        map[NodeTan::class] = nodeTrigService

        //------------------------ Math
        map[NodeGetVariable::class] = NodeGetVariableService()
        map[NodeCast::class] = NodeCastService(nodeHandlerService)

        //------------------------ Array
        val nodeArrayService = NodeArrayService(nodeHandlerService)
        map[NodeArrayFindElement::class] = nodeArrayService
        map[NodeArrayGetElement::class] = nodeArrayService
        map[NodeArraySize::class] = nodeArrayService


        //------------------------ Exec
        map[NodeArrayAssignElement::class] = NodeArrayAssignElementService(nodeHandlerService)
        map[NodeSetVariable::class] = NodeSetVariableService(nodeHandlerService)
        map[NodeArrayResize::class] = NodeArrayResizeService(nodeHandlerService)
        map[NodePrintString::class] = NodePrintStringService(nodeHandlerService, console)
        map[NodeInput::class] = NodeInputService(console)
        map[NodePrintString::class] = NodePrintStringService(nodeHandlerService, console)

        map[NodeMath::class] = NodeMathService(nodeHandlerService, MathInterpreterImpl())
        map[NodeSleep::class] = NodeSleepService(nodeHandlerService)
        return map
    }

}