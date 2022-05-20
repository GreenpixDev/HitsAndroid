package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_blueprint.*
import ru.hits.android.axolot.R
import ru.hits.android.axolot.custom_spinner.UITypes

class MacrosView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CreatorView(context, attrs) {

    lateinit var macrosName: String

    init {
        typeExpression = false
        initComponents()
        UITypes.images.add(R.drawable.ic_execute_variable)
        setCustomSpinner()
    }

    override fun addViewMenu() {
        activity.listMacros.addView(this)
    }

}