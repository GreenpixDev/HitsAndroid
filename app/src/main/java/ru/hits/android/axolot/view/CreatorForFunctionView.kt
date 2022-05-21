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
    var isMacros = false

    fun initComponents() {
        if (!isMacros) hideParametersExecuteForFunction()
    }

    fun hideParametersExecuteForFunction() {
        binding.inputExecuteRow.visibility = GONE
        binding.listInputExecuteVar.visibility = GONE
        binding.outputExecuteRow.visibility = GONE
        binding.listOutputExecuteVar.visibility = GONE
    }

}