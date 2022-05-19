package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.CreatorForFuncItemBinding

class CreatorForFunctionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = CreatorForFuncItemBinding.inflate(LayoutInflater.from(context), this)
}