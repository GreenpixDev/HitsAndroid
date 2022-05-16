package ru.hits.android.axolot.view

import android.content.Context
import ru.hits.android.axolot.BlueprintActivity
import ru.hits.android.axolot.blueprint.element.AxolotSource

interface BlueprintView {

    /**
     * Возвращает [Context] вьюшки
     */
    fun getContext(): Context

    /**
     * [BlueprintActivity] этой вьюшки
     */
    val activity: BlueprintActivity
        get() = getContext() as BlueprintActivity

    /**
     * [AxolotSource] для взаимодействия с промежуточными компонентами для компилятора
     */
    val sourceCode: AxolotSource
        get() = activity.program

}