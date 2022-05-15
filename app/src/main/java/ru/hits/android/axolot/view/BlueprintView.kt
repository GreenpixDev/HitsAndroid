package ru.hits.android.axolot.view

import android.content.Context
import com.otaliastudios.zoom.ZoomApi
import kotlinx.android.synthetic.main.activity_blueprint.*
import ru.hits.android.axolot.BlueprintActivity

interface BlueprintView {

    fun getContext(): Context

    val activity: BlueprintActivity
        get() = getContext() as BlueprintActivity

    val zoom: ZoomApi
        get() = activity.zoomLayout

}