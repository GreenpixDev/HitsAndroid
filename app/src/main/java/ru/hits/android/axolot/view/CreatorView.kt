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
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = CreatorItemBinding.inflate(LayoutInflater.from(context), this)

    var nameDescription = true
    var typeExpression = true
    var edit = true
    var inputRow = true
    var outputRow = true
    var isVar = false
    var btnAddDel = false

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
        if (!inputRow) binding.inputRow.visibility = GONE
        if (!outputRow) binding.outputRow.visibility = GONE
        if (btnAddDel) binding.btnAdd.visibility = GONE
        if (isVar) binding.creator.background = null
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