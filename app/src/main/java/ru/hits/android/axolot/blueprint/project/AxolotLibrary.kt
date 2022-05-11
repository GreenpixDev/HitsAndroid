package ru.hits.android.axolot.blueprint.project

import ru.hits.android.axolot.blueprint.declaration.DeclaredBlock
import ru.hits.android.axolot.blueprint.project.libs.AxolotDefaultLibrary

/**
 * Класс библиотеки для языка Axolot.
 * Можно подключать к другим библиотекам или программам.
 */
open class AxolotLibrary : AxolotProject {

    override val declarations = mutableMapOf<String, DeclaredBlock>()

    companion object {

        fun create(): AxolotLibrary {
            val library = AxolotLibrary()
            library.addLibrary(AxolotDefaultLibrary())
            return library
        }

    }

    override fun addLibrary(library: AxolotLibrary) {
        declarations.putAll(library.declarations)
    }

}