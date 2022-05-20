package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_blueprint.*

class FunctionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CreatorView(context, attrs) {

    lateinit var functionName: String

    init {
        typeExpression = false
        initComponents()
    }

    override fun addViewMenu() {
        activity.listFunction.addView(this)
    }

}