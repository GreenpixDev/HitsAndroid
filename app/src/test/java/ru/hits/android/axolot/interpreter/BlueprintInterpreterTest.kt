package ru.hits.android.axolot.interpreter

import org.junit.Test
import ru.hits.android.axolot.blueprint.node.NodeConstant
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.node.executable.NodePrintString
import ru.hits.android.axolot.blueprint.node.executable.NodeSetVariable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoop
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoopIndex
import ru.hits.android.axolot.blueprint.node.function.math.trig.NodeSin
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntSum
import ru.hits.android.axolot.blueprint.scope.GlobalScope
import ru.hits.android.axolot.blueprint.type.Type

class BlueprintInterpreterTest {

    @Test
    fun firstTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        val context = interpreter.createContext()

        scope.declareVariable("test", Type.INT, 2)

        val printString1 = NodePrintString()
        val castFloatToString1 = NodeCast(Type.STRING)
        val sin1 = NodeSin()
        val castIntToFloat1 = NodeCast(Type.FLOAT)
        val getVariableTest = NodeGetVariable("test")

        val setVariableTest = NodeSetVariable("test")
        val intSum1 = NodeIntSum()

        val printString2 = NodePrintString()
        val castIntToString1 = NodeCast(Type.STRING)

        val forLoop1 = NodeForLoop()
        val forLoop1Index = NodeForLoopIndex(forLoop1)
        val castIntToStringIndex = NodeCast(Type.STRING)
        val printString3 = NodePrintString()
        val printString4 = NodePrintString()

        printString1.init(castFloatToString1)
        castFloatToString1.init(sin1)
        sin1.init(castIntToFloat1)
        castIntToFloat1.init(getVariableTest)
        printString1.nextNode = setVariableTest

        setVariableTest.init(intSum1)
        intSum1.init(getVariableTest, NodeConstant.of(Type.INT, 2), NodeConstant.of(Type.INT, 10))
        setVariableTest.nextNode = printString2

        printString2.init(castIntToString1)
        castIntToString1.init(getVariableTest)
        printString2.nextNode = forLoop1

        forLoop1.init(NodeConstant.of(Type.INT, 1), getVariableTest)

        forLoop1.loopBody = printString3
        castIntToStringIndex.init(forLoop1Index)
        printString3.init(castIntToStringIndex)

        forLoop1.completed = printString4
        printString4.init(NodeConstant.of(Type.STRING, "ForLoop Completed!"))

        interpreter.execute(printString1, context)
    }
}