package ru.hits.android.axolot.save.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.hits.android.axolot.blueprint.declaration.BlockType
import java.lang.reflect.Type

class BlockTypeSerializer : JsonSerializer<BlockType> {

    override fun serialize(
        src: BlockType,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.fullName)
    }
}