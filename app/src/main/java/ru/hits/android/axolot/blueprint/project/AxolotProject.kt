package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.DeclaredBlock

/**
 * Общий интерфейс проекта на языке Axolot.
 * Это может быть библиотека или полноценная программа.
 */
interface AxolotProject {

    val declarations: MutableMap<String, DeclaredBlock>

    fun addLibrary(library: AxolotLibrary)

}