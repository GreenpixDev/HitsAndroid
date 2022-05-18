package ru.hits.android.axolot.save

import ru.hits.android.axolot.interpreter.type.Type

data class SerializedConstant(
    var type: Type,
    var value: Any?
)