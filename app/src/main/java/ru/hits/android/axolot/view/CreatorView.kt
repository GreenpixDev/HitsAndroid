package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.CreatorItemBinding

open class CreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = CreatorItemBinding.inflate(LayoutInflater.from(context), this)

    var nameDescription = true
    var typeExpression = true
    var edit = true

    private fun initLayoutParams() {
        val params = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        binding.rowForMenu.layoutParams = params
    }

    fun initComponents() {
        if (!nameDescription) binding.rowForMenu.removeView(binding.name)
        if (!typeExpression) binding.rowForMenu.removeView(binding.type)
        if (!edit) binding.rowForMenu.removeView(binding.btnEdit)
    }

    init {
        initLayoutParams()

    }
}