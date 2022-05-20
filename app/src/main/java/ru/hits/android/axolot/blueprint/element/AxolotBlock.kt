package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredDataPin
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.blueprint.element.pin.TypedPin
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.variable.Variable
import ru.hits.android.axolot.util.Vec2f

/**
 * Класс блока на плоскости.
 * Содержит в себе тип блока (декларацию блока), а также
 */
class AxolotBlock(val type: BlockType) : AxolotOwner {

    /**
     * Координата X для UI
     */
    var position: Vec2f? = null

    /**
     * Множество контактов (пинов) у блока
     */
    val contacts = linkedSetOf<Pin>()

    /**
     * Обновить блок, если обновился тип блока
     */
    fun update() {
        val contactsOnDelete = contacts.toMutableSet()

        type.declaredPins.forEach { declaredPin ->
            val needAdd = contacts
                .filterIsInstance<TypedPin>()
                .filter { it.type == declaredPin }
                .onEach {
                    contactsOnDelete.remove(it)
                }
                .isEmpty()

            if (needAdd) {
                declaredPin.createAllPin(this).forEach {
                    contacts.add(it)

                    if (it is InputDataPin) {
                        when ((it.type as DeclaredDataPin).type) {
                            Type.BOOLEAN -> it.adjacent =
                                ConstantPin(Variable(Type.BOOLEAN, false), it.owner)
                            Type.INT -> it.adjacent = ConstantPin(Variable(Type.INT, 0), it.owner)
                            Type.FLOAT -> it.adjacent =
                                ConstantPin(Variable(Type.FLOAT, 0.0), it.owner)
                            Type.STRING -> it.adjacent =
                                ConstantPin(Variable(Type.STRING, ""), it.owner)
                        }
                    }
                }
            }
        }

        contactsOnDelete.forEach { pinOnDelete ->
            pinOnDelete.clear()
            contacts.remove(pinOnDelete)
        }
    }
}