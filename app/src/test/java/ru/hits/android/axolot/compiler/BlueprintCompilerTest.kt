package ru.hits.android.axolot.compiler

import org.junit.Test
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin

import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.BlueprintInterpreter
import ru.hits.android.axolot.interpreter.scope.GlobalScope
import ru.hits.android.axolot.interpreter.type.Type

class BlueprintCompilerTest {

    @Test
    fun firstTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val mainBlock = program.mainBlock!!
        val printBlock = program.createBlock(program.blockTypes["native.print"]!!)

        program.addBlock(printBlock)

        val outPin = mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne
        val inPin = printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        outPin connect inPin

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World")

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console())

        interpreter.execute(node)
    }

    @Test
    fun functionTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val function = program.createFunction("test")
        val startFunction = function.beginBlock
        val printBlock = function.createBlock(program.blockTypes["native.print"]!!)

        val mainBlock = program.mainBlock!!
        val invokeBlock = program.createBlock(function)

        mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                invokeBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        startFunction.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World From Function")

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console())

        interpreter.execute(node)
    }

    @Test
    fun macrosTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val macros = program.createMacros("while")
        macros.addInputFlow("")

        val startMacros = macros.beginBlock
        val printBlock = macros.createBlock(program.blockTypes["native.print"]!!)

        val mainBlock = program.mainBlock!!
        val invokeMacros = program.createBlock(macros)

        mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                invokeMacros.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        startMacros.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World From Macros")

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console {
            it.invoke()
        })

        interpreter.execute(node)
    }

    /*@Test
    fun macrosForTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val macros = program.createMacros("while")
        macros.addInputFlow("")
        macros.addInputFlow("break")
        macros.addInputData("firstIndex", Type.INT)
        macros.addInputData("lastIndex", Type.INT)
        macros.addOutputFlow("loopBody")
        macros.addOutputFlow("completed")
        macros.addOutputData("index", Type.INT)

        val startMacros = macros.beginBlock
        val assignFirst = macros.createBlock(program.blockTypes["native.assignVariable.int"]!!)
        val localInt = macros.createBlock(program.blockTypes["native.localVariable.int"]!!)
        val lessOrEquals = macros.createBlock(program.blockTypes["native.math.int.lessOrEquals"]!!)
        val increment = macros.createBlock(program.blockTypes["native.math.int.sum"]!!)
        val assign = macros.createBlock(program.blockTypes["native.assignVariable.int"]!!)
        val branch = macros.createBlock(program.blockTypes["native.branch"]!!)
        val sequence = macros.createBlock(program.blockTypes["native.sequence"]!!)
        val endMacros = macros.endBlock

        startMacros.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                assignFirst.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        assignFirst.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                branch.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        branch.contacts.find { it is OutputPin && it.name == "false" }!! as PinToOne connect
                endMacros.contacts.find { it is InputPin && it.name == "completed" }!! as PinToMany
        branch.contacts.find { it is OutputPin && it.name == "true" }!! as PinToOne connect
                sequence.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        sequence.contacts.find { it is OutputPin && it.name == "then-1" }!! as PinToOne connect
                endMacros.contacts.find { it is InputPin && it.name == "loopBody" }!! as PinToMany
        sequence.contacts.find { it is OutputPin && it.name == "then-2" }!! as PinToOne connect
                assign.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        assign.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                branch.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        assignFirst.contacts.find { it is InputPin && it.name == "ref" }!! as PinToOne connect
                localInt.contacts.find { it is OutputPin && it.name == "" }!! as PinToMany
        assignFirst.contacts.find { it is InputPin && it.name == "value" }!! as PinToOne connect
                startMacros.contacts.find { it is OutputPin && it.name == "firstIndex" }!! as PinToMany
        branch.contacts.find { it is InputPin && it.name == "condition" }!! as PinToOne connect
                lessOrEquals.contacts.find { it is OutputPin && it.name == "" }!! as PinToMany
        branch.contacts.find { it is InputPin && it.name == "condition" }!! as PinToOne connect
                lessOrEquals.contacts.find { it is OutputPin && it.name == "" }!! as PinToMany

        val printBlock = macros.createBlock(program.blockTypes["native.print"]!!)

        val mainBlock = program.mainBlock!!
        val invokeMacros = program.createBlock(macros)

        mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                invokeMacros.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        startMacros.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World From Macros")

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console())

        interpreter.execute(node)
    }*/

    /*@Test
    fun fibonacciTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val function = program.createFunction("fibonacci")
        val startFunction = function.createBlock(function.beginType)

        val mainBlock = program.createBlock(program.blockTypes["native.main"]!!)
        val invokeBlock = program.createBlock(function)
        val printBlock = function.createBlock(program.blockTypes["native.print"]!!)

        startFunction.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        mainBlock.contacts.find { it is OutputFlowPin }!! as PinToOne connect
                invokeBlock.contacts.find { it is InputFlowPin }!! as PinToMany
        invokeBlock.contacts.find { it is OutputFlowPin }!! as PinToOne connect
                printBlock.contacts.find { it is InputFlowPin }!! as PinToMany

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World")

        program.mainBlock = mainBlock

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console())

        interpreter.execute(node)
    }*/

}