package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

abstract class RowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    abstract var description: Boolean
    abstract var descriptionText: String

    abstract fun initComponents()
}