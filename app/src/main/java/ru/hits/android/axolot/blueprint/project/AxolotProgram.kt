package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

/**
 * Класс исполняемой программы на языке Axolot.
 */
class AxolotProgram : AxolotProject, AxolotSource {

    override val blocks = hashSetOf<AxolotBlock>()

    @Deprecated("Пока не знаю, будет ли использоваться")
    override val contacts = hashSetOf<Pin>()

    override val variableTypes = mutableMapOf<String, VariableType<*>>()

    override val blockTypes = mutableMapOf<String, BlockType>()

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            return program
        }

    }

    /**
     * Добавить новый блок на плоскость
     */
    fun addBlock(block: AxolotBlock) {
        blocks.add(block)
        contacts.addAll(block.contacts)
    }

    /**
     * Задать значение булавке (если она не соединена с другими)
     */
    fun <T> setValue(pin: InputDataPin, type: VariableType<T>, value: T) {
        val constant = ConstantPin(Variable(type, value), pin.owner)
        pin.adjacent = constant
        contacts.add(constant)
    }

    /**
     * Соединить булавки
     */
    fun connect(from: PinToMany, to: PinToOne) {
        TODO()
    }

    /**
     * Соединить булавки
     */
    fun connect(from: PinToOne, to: PinToMany) {
        TODO()
    }
}