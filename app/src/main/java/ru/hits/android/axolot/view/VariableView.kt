package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet

class VariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : CreatorView(context, attrs) {

    lateinit var variableName: String

}