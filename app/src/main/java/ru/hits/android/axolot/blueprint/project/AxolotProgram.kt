package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.DeclaredBlock
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

    override val contacts = hashSetOf<Pin>()

    override val declarations = mutableMapOf<String, DeclaredBlock>()

    companion object {

        fun create(): AxolotProgram {
            val program = AxolotProgram()
            program.addLibrary(AxolotDefaultLibrary())
            return program
        }

    }

    /**
     * Добавить библиотеку в проект
     */
    override fun addLibrary(library: AxolotLibrary) {
        declarations.putAll(library.declarations)
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