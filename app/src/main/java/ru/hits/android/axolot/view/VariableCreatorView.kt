package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.VariableCreatorItemBinding

class VariableCreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = VariableCreatorItemBinding.inflate(LayoutInflater.from(context), this)

    fun getNameVariable(): String {
        val nameVar = binding.nameVariable.text

        return nameVar.toString()
    }

    fun getTypeVariable(): String {
        return binding.typeVariable.selectedItem.toString()
    }

    init {
        //что-то
    }
}