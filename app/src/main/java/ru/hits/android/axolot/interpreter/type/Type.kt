package ru.hits.android.axolot.interpreter.type

import ru.hits.android.axolot.interpreter.type.primitive.BooleanType
import ru.hits.android.axolot.interpreter.type.primitive.FloatType
import ru.hits.android.axolot.interpreter.type.primitive.IntType
import ru.hits.android.axolot.interpreter.type.primitive.StringType

interface Type {

    companion object {

        val BOOLEAN = BooleanType()
        val INT = IntType()
        val FLOAT = FloatType()
        val STRING = StringType()

    }

}