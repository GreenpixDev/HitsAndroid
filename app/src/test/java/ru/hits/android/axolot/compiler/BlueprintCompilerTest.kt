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

        val mainBlock = program.blockTypes["native.main"]!!.createBlock()
        val printBlock = program.blockTypes["native.print"]!!.createBlock()

        program.addBlock(mainBlock)
        program.addBlock(printBlock)

        val outPin = mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne
        val inPin = printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        outPin connect inPin

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World")

        program.mainBlock = mainBlock

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
        val startFunction = function.createBlock(function.beginType)
        val printBlock = function.createBlock(program.blockTypes["native.print"]!!)

        val mainBlock = program.createBlock(program.blockTypes["native.main"]!!)
        val invokeBlock = program.createBlock(function)

        mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                invokeBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        startFunction.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne connect
                printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World")

        program.mainBlock = mainBlock

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, Console())

        interpreter.execute(node)
    }

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