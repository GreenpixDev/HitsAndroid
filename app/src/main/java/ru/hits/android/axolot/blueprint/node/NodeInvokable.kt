package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.blueprint.context.Context

interface NodeInvokable<T> : Node {

    operator fun invoke(context: Context): T

}