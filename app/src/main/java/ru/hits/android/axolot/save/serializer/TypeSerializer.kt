package ru.hits.android.axolot.save.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.hits.android.axolot.interpreter.type.Type

class TypeSerializer : JsonSerializer<Type> {

    override fun serialize(
        src: Type,
        typeOfSrc: java.lang.reflect.Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}