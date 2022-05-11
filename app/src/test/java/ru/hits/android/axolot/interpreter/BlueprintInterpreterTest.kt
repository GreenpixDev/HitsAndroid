package ru.hits.android.axolot.interpreter

import org.junit.Test
import ru.hits.android.axolot.blueprint.element.BlueprintFunction
import ru.hits.android.axolot.blueprint.element.BlueprintMacros
import ru.hits.android.axolot.blueprint.node.NodeConstant
import ru.hits.android.axolot.blueprint.node.function.NodeCast
import ru.hits.android.axolot.blueprint.node.executable.NodePrintString
import ru.hits.android.axolot.blueprint.node.executable.NodeSetVariable
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayAssignElement
import ru.hits.android.axolot.blueprint.node.executable.array.NodeArrayResize
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoop
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeForLoopIndex
import ru.hits.android.axolot.blueprint.node.flowcontrol.NodeSequence
import ru.hits.android.axolot.blueprint.node.function.math.trig.NodeSin
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.node.function.array.NodeArrayGetElement
import ru.hits.android.axolot.blueprint.node.function.array.NodeArraySize
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.blueprint.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.blueprint.node.function.math.bool.NodeBooleanAnd
import ru.hits.android.axolot.blueprint.node.function.math.bool.NodeBooleanNot
import ru.hits.android.axolot.blueprint.node.function.math.bool.NodeBooleanOr
import ru.hits.android.axolot.blueprint.node.function.math.integer.*
import ru.hits.android.axolot.blueprint.node.macros.*
import ru.hits.android.axolot.blueprint.scope.GlobalScope
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.type.structure.ArrayType
import ru.hits.android.axolot.blueprint.variable.Variable

class BlueprintInterpreterTest {

    @Test
    fun firstTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)

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

        val forLoop1Index = NodeForLoopIndex()
        val forLoop1 = NodeForLoop(forLoop1Index)
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

        interpreter.execute(printString1)
    }

    @Test
    fun printTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        scope.declareVariable("str", Type.STRING, "hello")

        val nodeGetVariable = NodeGetVariable("str")

        val printNode = NodePrintString()
        printNode.init(nodeGetVariable)

        interpreter.execute(printNode)
    }

    @Test
    fun whileMacrosTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
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
        interpreter.execute(printStart)
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

        /*
         Делаем макрос
         */
        val macros = forLoop()

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
        interpreter.execute(printStart)
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
        interpreter.execute(printStart)
    }

    /*
     * Start Fibonacci Function Test!
     * Completed Fibonacci Function!
     */
    @Test
    fun fibonacciFunctionTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)

        /*
         Делаем функцию
         */

        // Объявляем все узлы
        val branch = NodeBranch()

        val or = NodeBooleanOr()
        val equals1 = NodeIntEqual()
        val equals2 = NodeIntEqual()

        val function = BlueprintFunction(branch)
        function.input["number"] = NodeFunctionParameter()
        function.output["return"] = Type.INT

        val return1 = NodeFunctionEnd(function)
        val return2 = NodeFunctionEnd(function)

        val invoke1 = NodeFunctionInvoke(function)
        val returned1 = NodeFunctionReturned(invoke1, "return")

        val invoke2 = NodeFunctionInvoke(function)
        val returned2 = NodeFunctionReturned(invoke2, "return")

        val sub1 = NodeIntSub()
        val sub2 = NodeIntSub()
        val sum = NodeIntSum()

        // Связка узлов
        equals1.init(function.input["number"]!!, NodeConstant.of(Type.INT, 1))
        equals2.init(function.input["number"]!!, NodeConstant.of(Type.INT, 2))
        or.init(equals1, equals2)
        branch.init(or)
        branch.trueNode = return1
        branch.falseNode = invoke1

        return1.dependencies["return"] = NodeConstant.of(Type.INT, 1)

        sub1.init(function.input["number"]!!, NodeConstant.of(Type.INT, 1))
        invoke1.dependencies["number"] = sub1
        invoke1.nextNode = invoke2

        sub2.init(sub1, NodeConstant.of(Type.INT, 1))
        invoke2.dependencies["number"] = sub2
        invoke2.nextNode = return2

        sum.init(returned1, returned2)
        return2.dependencies["return"] = sum

        /*
         Используем его
         */
        val printStart = NodePrintString()                      // Нода вывода в консоль начала программы
        val printResult = NodePrintString()                     // Нода вывода числа фибоначчи
        val printEnd = NodePrintString()                        // Нода вывода в консоль завершения программы
        val fibonacciInvoke = NodeFunctionInvoke(function)      // Нода вызова функции фибоначчи
        val fibonacciReturned = NodeFunctionReturned(fibonacciInvoke, "return")
        val intToString = NodeCast(Type.STRING)                 // Нода преобразования: int -> string

        printStart.init(NodeConstant.of(Type.STRING, "Start Fibonacci Function Test!"))
        printStart.nextNode = fibonacciInvoke

        fibonacciInvoke.dependencies["number"] = NodeConstant.of(Type.INT, 30)
        fibonacciInvoke.nextNode = printResult

        intToString.init(fibonacciReturned)
        printResult.init(intToString)
        printResult.nextNode = printEnd

        printEnd.init(NodeConstant.of(Type.STRING, "End Fibonacci Function Test!"))

        /*
         Запуск
         */
        val timestamp = System.currentTimeMillis()
        interpreter.execute(printStart)
        println("Executed for ${System.currentTimeMillis() - timestamp} ms")
    }

    /*
     * Start Fibonacci Function Test!
     * Completed Fibonacci Function!
     */
    @Test
    fun arrayTest() {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)
        scope.declareVariable("array", Variable.arrayVariable(Type.STRING, 1))

        /*
         Массив
         */
        val printStart = NodePrintString()
        val printArraySize = NodePrintString()
        val printEnd = NodePrintString()

        val getArray = NodeGetVariable("array")
        val arraySize = NodeArraySize()
        val intToString = NodeCast(Type.STRING)

        val arrayResize = NodeArrayResize()
        val assign1 = NodeArrayAssignElement()
        val assign2 = NodeArrayAssignElement()
        val assign3 = NodeArrayAssignElement()

        var forLoop = forLoop()
        var sub = NodeIntSub()
        var printElement = NodePrintString()
        var arrayGet = NodeArrayGetElement()

        // Начало
        printStart.init(NodeConstant.of(Type.STRING, "Start Array Test!"))
        printStart.nextNode = printArraySize

        // Выводим размер
        arraySize.init(getArray)
        intToString.init(arraySize)
        printArraySize.init(intToString)
        printArraySize.nextNode = arrayResize

        // Resize и добавляем элементы
        arrayResize.init(getArray, NodeConstant.of(Type.INT, 3))
        assign1.init(getArray, NodeConstant.of(Type.INT, 0), NodeConstant.of(Type.STRING, "How"))
        assign2.init(getArray, NodeConstant.of(Type.INT, 1), NodeConstant.of(Type.STRING, "are"))
        assign3.init(getArray, NodeConstant.of(Type.INT, 2), NodeConstant.of(Type.STRING, "you?"))

        arrayResize.nextNode = assign1
        assign1.nextNode = assign2
        assign2.nextNode = assign3
        assign3.nextNode = forLoop.inputExecutable["input"]

        // Цикл
        forLoop.outputExecutable["completed"]?.nextNode = printEnd
        forLoop.outputExecutable["loopBody"]?.nextNode = printElement
        forLoop.input["firstIndex"]?.init(NodeConstant.of(Type.INT, 0))
        forLoop.input["lastIndex"]?.init(sub)
        sub.init(arraySize, NodeConstant.of(Type.INT, 1))

        arrayGet.init(getArray, forLoop.output["index"]!!)
        printElement.init(arrayGet)

        // Конец
        printEnd.init(NodeConstant.of(Type.STRING, "End Array Test!"))

        /*
         Запуск
         */
        val timestamp = System.currentTimeMillis()
        interpreter.execute(printStart)
        println("Executed for ${System.currentTimeMillis() - timestamp} ms")
    }

    private fun forLoop(): BlueprintMacros {
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

        return macros
    }
}