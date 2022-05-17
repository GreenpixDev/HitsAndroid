package ru.hits.android.axolot.custom_spinner

import ru.hits.android.axolot.R

data class Type(val image: Int, val name: String)

object Types {

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

    var list: ArrayList<Type>? = null
        get() {
            if (field != null) return field

            field = ArrayList()
            for (ind in images.indices) {
                val typeVariable = Type(images[ind], types[ind])
                field!!.add(typeVariable)
            }

            return field
        }
}