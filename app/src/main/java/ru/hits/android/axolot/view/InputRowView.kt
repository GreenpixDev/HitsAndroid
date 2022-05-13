package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.InputRowItemBinding

@SuppressLint("RtlHardcoded")
class InputRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = InputRowItemBinding.inflate(LayoutInflater.from(context), this)
    private val params = LinearLayoutCompat.LayoutParams(
        LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.LEFT
        weight = 3f
    }

    var inputNode = true
    var description = true
    var descriptionText = "BlaBla"


    fun initComponents() {
        if (!inputNode) {
            binding.inputRowBlock.removeView(binding.inputNode)
        }

        if (!description) {
            binding.inputRowBlock.removeView(binding.descriptionTextView)
            //binding.descriptionTextView.visibility = GONE
        }

//        if (description) {
//            binding.descriptionTextView.visibility = VISIBLE
//        }

        setDescription()
    }

    private fun setDescription() {
        if (description) binding.descriptionTextView.text = descriptionText
    }

    init {
//        binding.inputNode.layoutParams = params
//        binding.descriptionTextView.layoutParams = params

        initComponents()
        setDescription()
    }
}