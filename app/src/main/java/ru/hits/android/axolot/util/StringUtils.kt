package ru.hits.android.axolot.util

/**
 * Генерация уникального имени переменной
 */
fun generateName(prefix: String, condition: (String) -> Boolean): String {
    if (condition.invoke(prefix)) {
        return prefix
    }
    var counter = 0

    while (!condition.invoke("$prefix$counter")) {
        counter++
    }
    return "$prefix$counter"
}