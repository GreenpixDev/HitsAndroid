package ru.hits.android.axolot.interpreter.variable

import ru.hits.android.axolot.interpreter.type.VariableType
import ru.hits.android.axolot.interpreter.type.primitive.BooleanType
import ru.hits.android.axolot.interpreter.type.primitive.FloatType
import ru.hits.android.axolot.interpreter.type.primitive.IntType
import ru.hits.android.axolot.interpreter.type.primitive.StringType
import ru.hits.android.axolot.interpreter.type.structure.ArrayType
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput
import java.io.Serializable

class Variable(var type: VariableType<*>, var value: Any?) : Serializable {


    @Suppress("unchecked_cast")
    operator fun <T> get(type: VariableType<T>): T? {
        require(this.type == type) { "invalid type of variable: excepted $type, actual ${this.type}" }
        return value as T
    }

    @Suppress("unchecked_cast")
    fun getArray(): Array<Variable>? {
        require(this.type is ArrayType) { "invalid type of variable: excepted Array<*>, actual ${this.type}" }
        return value as Array<Variable>?
    }

    companion object {

        fun defaultVariable(type: VariableType<*>): Variable {
            return Variable(type, type.defaultValue)
        }

        fun nullVariable(type: VariableType<*>): Variable {
            return Variable(type, null)
        }

        fun arrayVariable(type: VariableType<*>, size: Int): Variable {
            return Variable(ArrayType(type), Array(size) { nullVariable(type) })
        }

    }

}