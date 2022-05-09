package ru.hits.android.axolot.compiler

import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.interpreter.node.NodeExecutable

/**
 * Интерфейс компилятора для преобразования данных,
 * более менее удобных для UI в данные,
 * которые будет кушать интерпретатор
 */
interface Compiler {

    /**
     * Компилирует исходный код в интерпретируемые узлы
     */
    fun compile(source: AxolotSource): NodeExecutable?

}