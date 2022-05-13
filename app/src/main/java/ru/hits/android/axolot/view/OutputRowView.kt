package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.output_row_item.view.*
import ru.hits.android.axolot.databinding.OutputRowItemBinding

@SuppressLint("RtlHardcoded")
class OutputRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = OutputRowItemBinding.inflate(LayoutInflater.from(context), this)
    val params = LinearLayoutCompat.LayoutParams(
        LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.RIGHT
        marginStart = 10
    }
    var outputNode = true
    var description = true
    var descriptionText = "BlaBla"

    init {
        binding.outputRowBlock.layoutParams = params
        outputRowBlock.layoutParams

        initComponents()
        setDescription()
    }

    fun initComponents() {
        if (!outputNode) {
            binding.outputRowBlock.removeView(binding.outputNode)
        }

        if (!description) {
            binding.outputRowBlock.removeView(binding.descriptionTextView)
        }
        setDescription()
    }

    private fun setDescription() {
        if (description) binding.descriptionTextView.text = descriptionText
    }

}