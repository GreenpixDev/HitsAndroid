package ru.hits.android.axolot.view

import android.content.Context
import ru.hits.android.axolot.BlueprintActivity

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

}