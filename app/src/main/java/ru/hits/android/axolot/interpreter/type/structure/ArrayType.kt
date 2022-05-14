package ru.hits.android.axolot.interpreter.type.structure

import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.variable.Variable

class ArrayType(val elementType: VariableType<*>) : VariableType<Array<Variable>> {

    @Suppress("unchecked_cast")
    override fun cast(variable: Variable): Array<Variable> {
        return variable.value as Array<Variable>
    }

    override fun toString(): String {
        return "Array<$elementType>"
    }
}