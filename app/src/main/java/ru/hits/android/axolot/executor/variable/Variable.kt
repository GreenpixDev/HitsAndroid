package ru.hits.android.axolot.executor.variable

interface Variable<T> {

    val type: String

    var value: T?

}