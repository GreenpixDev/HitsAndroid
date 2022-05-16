package ru.hits.android.axolot.compiler

import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.interpreter.node.NodeExecutable

/**
 * Интерфейс компилятора для преобразования более менее удобных для UI данных
 * в узлы, которые будет кушать интерпретатор
 */
interface Compiler {

    /**
     * Компилирует исходный код программы в интерпретируемые узлы
     */
    fun compile(program: AxolotProgram): NodeExecutable?

}