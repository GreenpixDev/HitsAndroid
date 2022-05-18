package ru.hits.android.axolot.SaveService

import org.junit.Test
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.interpreter.BlueprintInterpreter
import ru.hits.android.axolot.interpreter.scope.GlobalScope
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.save.SaveService

class SaveServiceTest {
    @Test
    fun test(){
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

        val saveService = SaveService()
        saveService.saveProject(program, "save")
        val newProgram = saveService.loadProject("save")

        val node = compiler.compile(newProgram)
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope)

        interpreter.execute(node)
    }
}