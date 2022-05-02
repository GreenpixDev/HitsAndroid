package ru.hits.android.axolot.blueprint.context

import ru.hits.android.axolot.blueprint.node.Node
import java.io.Closeable

class LocalContext(private val parentContext: Context,
                   private val node: Node,
                   private val map: MutableMap<Any, Any?>) : Closeable {

    operator fun get(key: Any): Any? {
        return map[key]
    }

    operator fun set(key: Any, value: Any?) {
        map[key] = value
    }

    override fun close() {
        parentContext.local.remove(node)
    }
}