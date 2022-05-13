package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ru.hits.android.axolot.databinding.InputRowItemBinding

@SuppressLint("RtlHardcoded")
class InputRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val binding = InputRowItemBinding.inflate(LayoutInflater.from(context), this)
    var inputNode = true
    var description = true
    var descriptionText = "BlaBla"

    init {
//        val paramsInput = LinearLayout.LayoutParams()
//        paramsInput.= Gravity.LEFT

        initComponents()
        setDescription()
    }

    fun initComponents() {
        if (!inputNode) {
            binding.inputRowBlock.removeView(binding.inputNode)
            //binding.rowBlock.removeViewAt(0)
        }

        if (!description) {
            binding.descriptionTextView.visibility = GONE
        }

        if (description) {
            binding.descriptionTextView.visibility = VISIBLE
        }

        setDescription()
    }

    private fun setDescription() {
        if (description) binding.descriptionTextView.text = descriptionText
    }
}