package ru.hits.android.axolot.executor.node

interface NodeInvocable<T> : Node {

    fun invoke(): T

}