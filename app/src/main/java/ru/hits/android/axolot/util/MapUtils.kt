package ru.hits.android.axolot.util

/**
 * Отфильтровать словарь по критерию:
 * убрать из словаря элементы, значения которых равны null.
 */
fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    return this
        .filterValues { it != null }
        .mapValues { it.value!! }
}

/**
 * Отфильтровать словарь по критерию:
 * убрать из словаря элементы, ключи которых равны null.
 */
fun <K, V> Map<K?, V>.filterKeysNotNull(): Map<K, V> {
    return this
        .filterKeys { it != null }
        .mapKeys { it.key!! }
}

/**
 * Отфильтровать словарь по критерию:
 * оставить в словаре элементы, ключи которых принадлежат типу K;
 * оставить в словаре элементы, значения которых принадлежат типу V.
 * Затем преобразует исходный словарь в словарь с нужными типами.
 */
inline fun <reified K, reified V> Map<*, *>.filterIsInstance(): Map<K, V> {
    return this
        .filter { it.key is K && it.value is V }
        .mapKeys { it.key as K }
        .mapValues { it.value as V }
}

fun <E, K, V> MutableMap<E, MutableMap<K, V>>.putMap(entry: Map.Entry<E, Map<K, V>>) {
    val map: MutableMap<K, V>
    if (entry.key in this) {
        map = this[entry.key]!!
    } else {
        map = hashMapOf()
        this[entry.key] = map
    }
    map.putAll(entry.value)
}

fun <E> Iterable<E>.associateWithIndex(): Map<E, Int> {
    var index = 0
    return associateWith { index++ }
}