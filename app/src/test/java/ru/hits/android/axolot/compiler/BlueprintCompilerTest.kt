package ru.hits.android.axolot.compiler

import org.junit.Test
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin

import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.interpreter.BlueprintInterpreter
import ru.hits.android.axolot.interpreter.scope.GlobalScope
import ru.hits.android.axolot.interpreter.type.Type

class BlueprintCompilerTest {

    @Test
    fun firstTest() {
        val compiler = BlueprintCompiler()
        val program = AxolotProgram.create()

        val mainBlock = program.declarations["main"]!!.createBlock()
        val printBlock = program.declarations["print"]!!.createBlock()

        program.addBlock(mainBlock)
        program.addBlock(printBlock)

        val outPin = mainBlock.contacts.find { it is OutputPin && it.name == "" }!! as PinToOne
        val inPin = printBlock.contacts.find { it is InputPin && it.name == "" }!! as PinToMany
        outPin connect inPin

        val textPin =
            printBlock.contacts.find { it is InputPin && it.name == "text" }!! as InputDataPin
        program.setValue(textPin, Type.STRING, "Hello World")

        val node = compiler.compile(program)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)

        interpreter.execute(node)
    }

}