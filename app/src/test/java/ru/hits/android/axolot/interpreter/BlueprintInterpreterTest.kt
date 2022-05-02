package ru.hits.android.axolot.interpreter

import org.junit.Test
import ru.hits.android.axolot.blueprint.element.BlueprintMacros
import ru.hits.android.axolot.blueprint.node.NodeConstant
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.node.executable.NodePrintString
import ru.hits.android.axolot.blueprint.node.executable.NodeSetVariable
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoop
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoopIndex
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeSequence
import ru.hits.android.axolot.blueprint.node.function.math.trig.NodeSin
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.node.function.math.bool.NodeBooleanAnd
import ru.hits.android.axolot.blueprint.node.function.math.bool.NodeBooleanNot
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntLess
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntLessOrEqual
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntMore
import ru.hits.android.axolot.blueprint.node.function.math.integer.NodeIntSum
import ru.hits.android.axolot.blueprint.node.macros.*
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

    @Test
    fun whileMacrosTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        val context = interpreter.createContext()
        scope.declareVariable("counter", Type.INT, 0)

        /*
         Делаем макрос
         */
        val macros = BlueprintMacros()
        macros.inputExecutable["input"] = NodeMacrosInput()
        macros.input["condition"] = NodeMacrosDependency()
        macros.outputExecutable["loopBody"] = NodeMacrosOutput()
        macros.outputExecutable["completed"] = NodeMacrosOutput()

        val branch = NodeBranch()                               // нода IF
        val sequence = NodeSequence()                           // нода последовательности

        branch.init(macros.input["condition"]!!)                // Проверяем условие
        branch.trueNode = sequence                              // Если true - переходим к ноде последовательности
        branch.falseNode = macros.outputExecutable["completed"] // Если false - цикл завершен

        sequence.then(macros.outputExecutable["loopBody"]!!)    // Сначала цикл
        sequence.then(branch)                                   // Потом опять IF

        macros.inputExecutable["input"]?.nextNode = branch      // Макрос будет начинаться с IF

        /*
         Используем его
         */
        val printStart = NodePrintString()                      // Нода вывода в консоль начала программы
        val printLoopBody = NodePrintString()                   // Нода вывода в консоль итерации
        val printCompleted = NodePrintString()                  // Нода вывода в консоль завершения цикла
        val intToString = NodeCast(Type.STRING)                 // Нода int -> string
        val getVariable = NodeGetVariable("counter")            // Нода getCounter()
        val ifLess = NodeIntLess()                              // Нода int < int
        val setVariable = NodeSetVariable("counter")            // Нода setCounter(..)
        val sum = NodeIntSum()                                  // Нода int + int

        ////
        // Выводим сообщение и вызываем макрос
        printStart.init(NodeConstant.of(Type.STRING, "Start While Macros Test!"))
        printStart.nextNode = macros.inputExecutable["input"]
        ////

        ////
        // Преобразуем counter в string и выводим в консоль
        intToString.init(getVariable)
        printLoopBody.init(intToString)

        // counter = counter + 1
        sum.init(getVariable, NodeConstant.of(Type.INT, 1))
        setVariable.init(sum)
        printLoopBody.nextNode = setVariable

        // Эти действия повторяются на каждой итерации
        macros.outputExecutable["loopBody"]?.nextNode = printLoopBody
        ////

        ////
        // Выводим, что цикл завершен
        printCompleted.init(NodeConstant.of(Type.STRING, "Completed While Macros!"))

        // Это действие будет вызываться, когда цикл завершен
        macros.outputExecutable["completed"]?.nextNode = printCompleted
        ////

        ////
        // Проверяем counter < 0
        ifLess.init(getVariable, NodeConstant.of(Type.INT, 10))

        // Эта проверка для макроса while
        macros.input["condition"]?.init(ifLess)
        ////

        /*
         Запуск
         */
        interpreter.execute(printStart, context)
    }

    /*
    * Start While Macros Test!
    * 5
    * 6
    * 7
    * 8
    * 9
    * 10
    * 11
    * 12
    * 13
    * 14
    * 15
    * Completed While Macros!
    */
    @Test
    fun forMacrosTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        val context = interpreter.createContext()

        /*
         Делаем макрос
         */
        val macros = BlueprintMacros()
        macros.inputExecutable["input"] = NodeMacrosInput()
        macros.input["firstIndex"] = NodeMacrosDependency()
        macros.input["lastIndex"] = NodeMacrosDependency()
        macros.outputExecutable["loopBody"] = NodeMacrosOutput()
        macros.outputExecutable["completed"] = NodeMacrosOutput()
        macros.output["index"] = NodeMacrosDependency()

        val localInt = NodeLocalVariable(Type.INT)              // Нода локальной переменной localInt
        val firstAssign = NodeAssignVariable()                  // Нода присваивания
        val lessOrEqual = NodeIntLessOrEqual()                  // Нода a <= b
        val branch = NodeBranch()                               // Нода IF
        val sequence = NodeSequence()                           // Нода последовательности
        val loopAssign = NodeAssignVariable()                   // Нода присваивания
        val sum = NodeIntSum()                                  // Нода a + b

        firstAssign.init(localInt, macros.input["firstIndex"]!!)// localInt = lastIndex
        firstAssign.nextNode = branch                           // Далее IF

        lessOrEqual.init(localInt, macros.input["lastIndex"]!!) // localInt <= lastIndex

        branch.init(lessOrEqual)                                // Проверяем условие
        branch.trueNode = sequence                              // Если true - переходим к ноде последовательности
        branch.falseNode = macros.outputExecutable["completed"] // Если false - цикл завершен

        sequence.then(macros.outputExecutable["loopBody"]!!)    // Сначала цикл
        sequence.then(loopAssign)                               // Потом assign

        loopAssign.init(localInt, sum)                          // localInt++
        sum.init(localInt, NodeConstant.of(Type.INT, 1))
        loopAssign.nextNode = branch                            // Потом снова IF

        macros.inputExecutable["input"]?.nextNode = firstAssign // Макрос будет начинаться с Assign
        macros.output["index"]?.init(localInt)                  // На выход макрос отдаёт localInt

        /*
         Используем его
         */
        val printStart = NodePrintString()                      // Нода вывода в консоль начала программы
        val printLoopBody = NodePrintString()                   // Нода вывода в консоль итерации
        val printCompleted = NodePrintString()                  // Нода вывода в консоль завершения цикла
        val intToString = NodeCast(Type.STRING)                 // Нода int -> string

        ////
        // Выводим сообщение и вызываем макрос
        printStart.init(NodeConstant.of(Type.STRING, "Start For Macros Test!"))
        printStart.nextNode = macros.inputExecutable["input"]

        // На вход макросу константы
        macros.input["firstIndex"]?.init(NodeConstant.of(Type.INT, 5))
        macros.input["lastIndex"]?.init(NodeConstant.of(Type.INT, 15))
        ////

        ////
        // Преобразуем index в string и выводим в консоль
        intToString.init(macros.output["index"]!!)
        printLoopBody.init(intToString)

        // Эти действия повторяются на каждой итерации
        macros.outputExecutable["loopBody"]?.nextNode = printLoopBody
        ////

        ////
        // Выводим, что цикл завершен
        printCompleted.init(NodeConstant.of(Type.STRING, "Completed For Macros!"))

        // Это действие будет вызываться, когда цикл завершен
        macros.outputExecutable["completed"]?.nextNode = printCompleted
        ////

        /*
         Запуск
         */
        interpreter.execute(printStart, context)
    }

    /*
     * Start While Macros Test!
     * 5
     * 6
     * 7
     * 8
     * 9
     * 10
     * 11
     * 12
     * 13
     * 14
     * 15
     * Completed While Macros!
     */
    @Test
    fun forWithBreakMacrosTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        val context = interpreter.createContext()

        /*
         Делаем макрос
         */
        val macros = BlueprintMacros()
        macros.inputExecutable["input"] = NodeMacrosInput()
        macros.inputExecutable["break"] = NodeMacrosInput()
        macros.input["firstIndex"] = NodeMacrosDependency()
        macros.input["lastIndex"] = NodeMacrosDependency()
        macros.outputExecutable["loopBody"] = NodeMacrosOutput()
        macros.outputExecutable["completed"] = NodeMacrosOutput()
        macros.output["index"] = NodeMacrosDependency()

        // Объявляем все узлы
        val localBreak = NodeLocalVariable(Type.BOOLEAN)            // Нода локальной переменной: boolean break
        val localIndex = NodeLocalVariable(Type.INT)                // Нода локальной переменной: int index
        val firstAssignBreak = NodeAssignVariable()                 // Нода присваивания: break = ..
        val firstAssignIndex = NodeAssignVariable()                 // Нода присваивания: index = ..

        val lessOrEqual = NodeIntLessOrEqual()                      // Нода <=: index <= lastIndex
        val not = NodeBooleanNot()                                  // Нода NOT: !break
        val and = NodeBooleanAnd()                                  // Нода AND: !break && index <= lastIndex
        val preBranch = NodeBranch()                                // Нода IF: if (!break && index <= lastIndex)

        val sequence = NodeSequence()                               // Нода последовательности
        val postBranch = NodeBranch()                               // Нода IF: if (!break)
        val assignIndex = NodeAssignVariable()                      // Нода присваивания: index = ..
        val sum = NodeIntSum()                                      // Нода SUM: index + 1

        val assignBreak = NodeAssignVariable()                      // Нода присваивания: break = ..

        // Связка узлов
        ////

        macros.inputExecutable["input"]?.nextNode = firstAssignBreak

        firstAssignBreak.init(localBreak, NodeConstant.of(Type.BOOLEAN, false))
        firstAssignBreak.nextNode = firstAssignIndex

        firstAssignIndex.init(localIndex, macros.input["firstIndex"]!!)
        firstAssignIndex.nextNode = preBranch

        not.init(localBreak)
        lessOrEqual.init(localIndex, macros.input["lastIndex"]!!)
        and.init(not, lessOrEqual)
        preBranch.init(and)
        preBranch.trueNode = sequence
        preBranch.falseNode = macros.outputExecutable["completed"]

        sequence.then(macros.outputExecutable["loopBody"]!!)
        sequence.then(postBranch)

        postBranch.init(not)
        postBranch.trueNode = assignIndex
        postBranch.falseNode = macros.outputExecutable["completed"]

        sum.init(localIndex, NodeConstant.of(Type.INT, 1))
        assignIndex.init(localIndex, sum)
        assignIndex.nextNode = preBranch

        ////

        macros.inputExecutable["break"]?.nextNode = assignBreak

        assignBreak.init(localBreak, NodeConstant.of(Type.BOOLEAN, true))

        ////

        macros.output["index"]?.init(localIndex)

        ////

        /*
         Используем его
         */
        val printStart = NodePrintString()                      // Нода вывода в консоль начала программы
        val printLoopBody = NodePrintString()                   // Нода вывода в консоль итерации
        val printCompleted = NodePrintString()                  // Нода вывода в консоль завершения цикла
        val intToString = NodeCast(Type.STRING)                 // Нода int -> string
        val branch = NodeBranch()                               // Нода IF
        val isMore = NodeIntMore()                              // Нода int > int

        ////
        // Выводим сообщение и вызываем макрос
        printStart.init(NodeConstant.of(Type.STRING, "Start For With Break Macros Test!"))
        printStart.nextNode = macros.inputExecutable["input"]

        // На вход макросу константы
        macros.input["firstIndex"]?.init(NodeConstant.of(Type.INT, 5))
        macros.input["lastIndex"]?.init(NodeConstant.of(Type.INT, 15))
        ////

        ////
        // Преобразуем index в string и выводим в консоль
        intToString.init(macros.output["index"]!!)
        printLoopBody.init(intToString)
        printLoopBody.nextNode = branch

        isMore.init(macros.output["index"]!!, NodeConstant.of(Type.INT, 10))
        branch.init(isMore)
        branch.trueNode = macros.inputExecutable["break"]

        // Эти действия повторяются на каждой итерации
        macros.outputExecutable["loopBody"]?.nextNode = printLoopBody
        ////

        ////
        // Выводим, что цикл завершен
        printCompleted.init(NodeConstant.of(Type.STRING, "Completed For With Break Macros!"))

        // Это действие будет вызываться, когда цикл завершен
        macros.outputExecutable["completed"]?.nextNode = printCompleted
        ////

        /*
         Запуск
         */
        interpreter.execute(printStart, context)
    }
}