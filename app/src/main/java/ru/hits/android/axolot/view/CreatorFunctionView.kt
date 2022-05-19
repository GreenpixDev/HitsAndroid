package ru.hits.android.axolot.view

import android.content.Context
import kotlinx.android.synthetic.main.activity_blueprint.*

class CreatorFunctionView(context: Context) : CreatorView(context), BlueprintView {

    override fun addViewMenu(view: CreatorView) {
        activity.listFunction.addView(view)
    }
}