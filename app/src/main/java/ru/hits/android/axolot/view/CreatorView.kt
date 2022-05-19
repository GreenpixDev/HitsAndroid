package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.custom_spinner.SpinnerCustomAdapter
import ru.hits.android.axolot.custom_spinner.Types
import ru.hits.android.axolot.databinding.CreatorItemBinding

open class CreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = CreatorItemBinding.inflate(LayoutInflater.from(context), this)

    var nameDescription = true
    var typeExpression = true
    var btnAddDel = false
    var edit = true
    var isVar = false

    private fun initLayoutParams() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            marginStart = 10
            marginEnd = 10
        }

        binding.rowForMenu.layoutParams = params
    }

    fun initComponents() {
        if (!nameDescription) binding.name.visibility = GONE
        if (!typeExpression) binding.typeVariable.visibility = GONE
        if (!edit) binding.btnEdit.visibility = GONE
        if (isVar) binding.creator.background = null
        if (btnAddDel) binding.btnAdd.visibility = GONE
    }

    private fun setCustomSpinner() {
        val adapter = SpinnerCustomAdapter(context, Types.list!!)
        binding.typeVariable.adapter = adapter
    }

    init {
        initLayoutParams()
        setCustomSpinner()
    }
}