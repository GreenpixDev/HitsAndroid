package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_blueprint.*

class MacrosView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CreatorView(context, attrs) {

    lateinit var macrosName: String

    init {
        typeExpression = false
        initComponents()
    }

    override fun addViewMenu() {
        activity.listMacros.addView(this)
    }

}