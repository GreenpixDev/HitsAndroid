package ru.hits.android.axolot.save.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.PinToMany
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.TypedPin
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.save.SerializedConstant
import ru.hits.android.axolot.save.SerializedPin
import ru.hits.android.axolot.util.associateWithIndex
import java.lang.reflect.Type

class AxolotBlockSerializer : JsonSerializer<AxolotBlock> {

    override fun serialize(
        src: AxolotBlock,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        var counter = 0
        val json = JsonObject()
        val declaredPin = src.type.declaredPins.associateWithIndex()
        val map = src.contacts.associateWith { SerializedPin(counter++, it.name) }

        map.forEach { e ->
            val pin = e.key
            if (pin is TypedPin) {
                declaredPin[pin.type]?.let { e.value.declaration = it }
            }
            if (pin is PinToMany) {
                pin.adjacent.forEach { adj -> map[adj]?.let { e.value.adjacent.add(it.id) } }
            }
            if (pin is PinToOne) {
                pin.adjacent?.let { adj ->
                    if (adj is ConstantPin) {
                        e.value.constant = SerializedConstant(adj.constant.type, adj.constant.value)
                    } else {
                        map[adj]?.let { e.value.adjacent.add(it.id) }
                    }
                }
            }
        }

        json.addProperty("x", src.x)
        json.addProperty("y", src.y)
        json.addProperty("type", src.type.fullName)
        json.add("pin", context.serialize(map.values))

        return json
    }
}