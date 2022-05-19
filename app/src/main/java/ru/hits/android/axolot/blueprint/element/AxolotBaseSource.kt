package ru.hits.android.axolot.blueprint.element

import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredDataPin
import ru.hits.android.axolot.blueprint.element.pin.*
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.exception.AxolotPinException
import ru.hits.android.axolot.exception.AxolotPinOneAdjacentException
import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

open class AxolotBaseSource : AxolotSource {

    override val blocks = hashSetOf<AxolotBlock>()

    override fun addBlock(block: AxolotBlock) {
        blocks.add(block)
    }

    override fun <T> setValue(pin: InputDataPin, type: VariableType<T>, value: T) {
        val constant = ConstantPin(Variable(type, value), pin.owner)
        pin.adjacent = constant
    }

    @Throws(AxolotPinException::class)
    override fun connect(from: Pin, to: Pin) {
        if ((from !is FlowPin || to !is FlowPin) && (from !is DataPin || to !is DataPin)) {
            throw AxolotPinException { "Pins must be of the same type" }
        }

        if (from is TypedPin && to is TypedPin) {
            val fromType = from.type
            val toType = to.type

            if (fromType is DeclaredDataPin && toType is DeclaredDataPin
                && fromType.type != toType.type
            ) {
                throw AxolotPinException { "Pins must be of the same variable type" }
            }
        }

        if (from is PinToOne && to is PinToMany) {
            if (from.adjacent != null && from.adjacent !is ConstantPin) {
                throw AxolotPinOneAdjacentException(from) { "Pin 'from' can have 1 adjacent pin" }
            }
            from connect to
            return
        }

        if (from is PinToMany && to is PinToOne) {
            if (to.adjacent != null && to.adjacent !is ConstantPin) {
                throw AxolotPinOneAdjacentException(to) { "Pin 'to' can have 1 adjacent pin" }
            }
            from connect to
            return
        }
        throw AxolotPinException { "Pins must be opposite in relationship type" }
    }

    override fun disconnect(pin: Pin) {
        if (pin is PinToOne) {
            pin.adjacent = null
        }
        if (pin is PinToMany) {
            pin.adjacent.clear()
        }
    }
}