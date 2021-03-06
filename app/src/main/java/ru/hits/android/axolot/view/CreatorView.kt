package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.custom_spinner.CustomAdapter
import ru.hits.android.axolot.custom_spinner.UITypes
import ru.hits.android.axolot.databinding.CreatorItemBinding

abstract class CreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs), BlueprintView {

    private val binding = CreatorItemBinding.inflate(LayoutInflater.from(context), this)

    var nameDescription = true
    var typeExpression = true
    var btnAddDel = false
    var edit = true
    var set = true
    var get = true
    var executeVar = false
    var isVar = false

    fun initLayoutParams() {
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
        if (!set) binding.btnSet.visibility = GONE
        if (!get) binding.btnGet.visibility = GONE
        if (isVar) binding.creator.background = null
        if (btnAddDel) binding.btnSet.visibility = GONE
        if (executeVar) {
            binding.typeExecute.visibility = VISIBLE
            binding.typeVariable.visibility = GONE
        }
    }

    fun setCustomSpinner() {
        val adapter = CustomAdapter(context, UITypes.list)
        binding.typeVariable.adapter = adapter
    }

    abstract fun addViewMenu()

    init {
        initLayoutParams()
        setCustomSpinner()
    }
}