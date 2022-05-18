package ru.hits.android.axolot.save.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import java.lang.reflect.Type

class AxolotBlockDeserializer : JsonDeserializer<AxolotBlock> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AxolotBlock {
        TODO()
        /*var counter = 0
        val json = JsonObject()
        val declaredPin = src.type.declaredPins.associateWithIndex()
        val map = src.contacts.associateWith { SerializedPin(counter++, it.name) }

        map.forEach { e ->
            val pin = e.key
            if (pin is TypedPin) {
                declaredPin[pin.type]?.let { e.value.declaration = it }
            }
            if (pin is PinToMany) {
                pin.adjacent.forEach { adj -> map[adj]?.let { e.value.adjacent.add(it.id) }}
            }
            if (pin is PinToOne) {
                pin.adjacent?.let { adj ->
                    if (adj is ConstantPin) {
                        e.value.constant = SerializedConstant(adj.constant.type, adj.constant.value)
                    }
                    else {
                        map[adj]?.let { e.value.adjacent.add(it.id) }
                    }
                }
            }
        }

        json.addProperty("x", src.x)
        json.addProperty("y", src.y)
        json.addProperty("type", src.type.fullName)
        json.add("pin", context.serialize(map))

        return json*/
    }
}