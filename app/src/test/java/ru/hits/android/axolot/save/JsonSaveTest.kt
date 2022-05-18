package ru.hits.android.axolot.save

import com.google.gson.GsonBuilder
import org.junit.Test
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.InputPin
import ru.hits.android.axolot.blueprint.element.pin.OutputPin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.save.serializer.AxolotBlockSerializer
import ru.hits.android.axolot.save.serializer.BlockTypeSerializer
import ru.hits.android.axolot.save.serializer.TypeSerializer

class JsonSaveTest {

    @Test
    fun jsonSave() {
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

        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(Type::class.java, TypeSerializer())
            .registerTypeAdapter(BlockType::class.java, BlockTypeSerializer())
            .registerTypeAdapter(AxolotBlock::class.java, AxolotBlockSerializer())
            .create()

        val json = gson.toJson(program)

        println()
        println("--- JSON START ---")
        println()
        println(json)
        println()
        println("--- JSON END ---")
        println()
    }

}