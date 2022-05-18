package ru.hits.android.axolot.compiler

import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.Interpreter
import ru.hits.android.axolot.interpreter.node.NodeExecutable

/**
 * Интерфейс компилятора для преобразования более менее удобных для UI данных
 * в узлы, которые будет кушать интерпретатор
 */
interface Compiler {

    /**
     * Подготавливает интерпретатор для программы
     */
    fun prepareInterpreter(program: AxolotProgram, console: Console): Interpreter

    /**
     * Компилирует исходный код программы в интерпретируемые узлы
     */
    fun compile(program: AxolotProgram): NodeExecutable?

}