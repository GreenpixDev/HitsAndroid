package ru.hits.android.axolot.blueprint.project.libs

import ru.hits.android.axolot.blueprint.project.AxolotLibrary

/**
 * Стандартная библиотека нашего языка для любой программы
 * TODO пока содержит только нативные элементы, потом добавим ещё наши
 */
class AxolotDefaultLibrary : AxolotLibrary() {

    init {
        addLibrary(AxolotNativeLibrary())
    }

}