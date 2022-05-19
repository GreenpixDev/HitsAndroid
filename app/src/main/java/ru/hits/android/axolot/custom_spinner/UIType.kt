package ru.hits.android.axolot.custom_spinner

import ru.hits.android.axolot.R

data class UIType(val image: Int, val name: String) {

    override fun toString() = name
}

object UITypes {

    private val images = intArrayOf(
        R.drawable.ic_boolean_variable,
        R.drawable.ic_int_variable,
        R.drawable.ic_float_variable,
        R.drawable.ic_string_variable
    )

    private val types = arrayOf(
        "Boolean",
        "Integer",
        "Float",
        "String"
    )

    var list: List<UIType> = images.indices.map {
        UIType(images[it], types[it])
    }
}