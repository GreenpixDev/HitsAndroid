package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_blueprint.*

class VariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : CreatorView(context, attrs) {

    lateinit var variableName: String

    override fun addViewMenu() {
        activity.listVariables.addView(this)
    }
}