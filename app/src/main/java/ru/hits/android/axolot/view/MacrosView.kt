package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet

class MacrosView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : CreatorView(context, attrs, defstyleAttr, defstyleRes) {

    lateinit var macrosName: String

    init {
        typeExpression = false
        initComponents()
    }

}