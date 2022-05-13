package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
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

    fun initLayoutParams() {
        val params = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.RIGHT
        }
    }

    init {
        //что-то
    }
}