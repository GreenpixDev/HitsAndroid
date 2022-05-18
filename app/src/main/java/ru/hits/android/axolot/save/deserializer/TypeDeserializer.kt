package ru.hits.android.axolot.save.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.hits.android.axolot.blueprint.project.AxolotProject
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.interpreter.type.VariableType

class TypeDeserializer(val project: AxolotProject) : JsonDeserializer<Type> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: java.lang.reflect.Type,
        context: JsonDeserializationContext
    ): VariableType<*>? {
        return project.variableTypes[json.asString]
    }
}