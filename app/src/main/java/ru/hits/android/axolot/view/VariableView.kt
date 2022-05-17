package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet

class VariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : CreatorView(context, attrs, defstyleAttr, defstyleRes) {

    lateinit var variableName: String

}