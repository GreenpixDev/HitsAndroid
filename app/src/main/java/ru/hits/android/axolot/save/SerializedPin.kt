package ru.hits.android.axolot.save

data class SerializedPin(
    var id: Int,
    var name: String,
    var declaration: Int = 0,
    var adjacent: MutableList<Int> = mutableListOf(),
    var constant: SerializedConstant? = null
)
