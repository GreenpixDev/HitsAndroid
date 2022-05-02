package ru.hits.android.axolot.blueprint.type

import ru.hits.android.axolot.blueprint.type.primitive.BooleanType
import ru.hits.android.axolot.blueprint.type.primitive.FloatType
import ru.hits.android.axolot.blueprint.type.primitive.IntType
import ru.hits.android.axolot.blueprint.type.primitive.StringType

interface Type {

    companion object {

        val BOOLEAN = BooleanType()
        val INT = IntType()
        val FLOAT = FloatType()
        val STRING = StringType()

    }

}