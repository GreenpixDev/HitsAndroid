package ru.hits.android.axolot.save.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.project.AxolotProject
import java.lang.reflect.Type

class BlockTypeDeserializer(val project: AxolotProject) : JsonDeserializer<BlockType> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BlockType? {
        return project.blockTypes[json.asString]
    }
}