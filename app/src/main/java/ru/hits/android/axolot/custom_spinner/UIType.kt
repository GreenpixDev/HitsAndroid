package ru.hits.android.axolot.custom_spinner

import ru.hits.android.axolot.R
import ru.hits.android.axolot.interpreter.type.Type

data class UIType(val image: Int, val name: String) {

    override fun toString() = name

}

object UITypes {

    val images = mutableListOf(
        R.drawable.ic_boolean_variable,
        R.drawable.ic_int_variable,
        R.drawable.ic_float_variable,
        R.drawable.ic_string_variable
    )

    private val types = arrayOf(
        Type.BOOLEAN.toString(),
        Type.INT.toString(),
        Type.FLOAT.toString(),
        Type.STRING.toString(),
        "Execute"
    )

    var list: List<UIType> = images.indices.map {
        UIType(images[it], types[it])
    }
}