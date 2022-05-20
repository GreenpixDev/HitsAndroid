package ru.hits.android.axolot.interpreter.type

import ru.hits.android.axolot.interpreter.type.primitive.BooleanType
import ru.hits.android.axolot.interpreter.type.primitive.FloatType
import ru.hits.android.axolot.interpreter.type.primitive.IntType
import ru.hits.android.axolot.interpreter.type.primitive.StringType
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput
import java.io.Serializable

interface Type : Externalizable {

    override fun readExternal(p0: ObjectInput?) {
        BOOLEAN = p0?.readObject() as BooleanType
        INT = p0?.readObject() as IntType
        FLOAT = p0?.readObject() as FloatType
        STRING = p0?.readObject() as StringType
    }

    override fun writeExternal(p0: ObjectOutput?) {
        p0?.writeObject(BOOLEAN)
        p0?.writeObject(INT)
        p0?.writeObject(FLOAT)
        p0?.writeObject(STRING)
    }

    companion object {

        var BOOLEAN = BooleanType()
        var INT = IntType()
        var FLOAT = FloatType()
        var STRING = StringType()

    }

}